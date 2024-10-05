package org.rhm.stock.handler.signal;

import java.util.List;
import java.util.NoSuchElementException;

import org.rhm.stock.domain.AveragePrice;
import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.service.AveragePriceService;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.SignalService;
import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("priceTrend")
public class PriceTrend implements SignalScanner {
	@Autowired
	private StatisticService statSvc;
	@Autowired
	private SignalService signalSvc;
	@Autowired
	private AveragePriceService avgPriceSvc;
	@Autowired
	private PriceService priceSvc;
	
	private static final String WEEKLY_CLOSE_STAT = "WKCLSPCT";
	private static final String UP_4_WEEKS_SIGNAL = "4WKUP";
	private static final String UP_5_WEEKS_SIGNAL = "5WKUP";
	private static final String DN_4_WEEKS_SIGNAL = "4WKDN";
	private static final String DN_5_WEEKS_SIGNAL = "5WKDN";
	private static final String PRICE_UPTREND_SIGNAL = "UPTREND";
	private static final String PRICE_DNTREND_SIGNAL = "DNTREND";
	private static final String VOL_UPTREND_SIGNAL = "VOLUPTREND";
	private static final String RR_TRACKS = "RRTRK";
	private static final String AVG_VOL_500K = "AVGVOL500K";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceTrend.class);
	
	private void detectConsecutiveUpWeeks(List<StockStatistic> weeklyPriceChgList) {
		int upCnt = 0;
		StockStatistic firstStat = weeklyPriceChgList.get(0);
		StockStatistic stat = null;
		StockPrice price = null;
		for (int i = 0; i < 25; i+=5) {
			stat = weeklyPriceChgList.get(i);
			if (stat.getStatisticValue() > 0) {
				upCnt++;
			}
			else {
				break;
			}
		}
		if (upCnt == 4) {
			price = priceSvc.findStockPrice(firstStat.getPriceId());
			signalSvc.createSignal(new StockSignal(price, UP_4_WEEKS_SIGNAL));
			LOGGER.debug("detectConsecutiveUpWeeks - 4 consecutive weeks up");
		}
		else {
			if (upCnt == 5) {
				price = priceSvc.findStockPrice(firstStat.getPriceId());
				signalSvc.createSignal(new StockSignal(price, UP_4_WEEKS_SIGNAL));
				signalSvc.createSignal(new StockSignal(price, UP_5_WEEKS_SIGNAL));
				LOGGER.debug("detectConsecutiveUpWeeks - 5 consecutive weeks up");
			}
		}
	}

	private void detectConsecutiveDownWeeks(List<StockStatistic> weeklyPriceChgList) {
		int downCnt = 0;
		StockStatistic firstStat = weeklyPriceChgList.get(0);
		StockStatistic stat = null;
		StockPrice price = null;
		for (int i = 0; i < 25; i+=5) {
			stat = weeklyPriceChgList.get(i);
			if (stat.getStatisticValue() < 0) {
				downCnt++;
			}
			else {
				break;
			}
		}
		if (downCnt == 4) {
			price = priceSvc.findStockPrice(firstStat.getPriceId());
			signalSvc.createSignal(new StockSignal(price, DN_4_WEEKS_SIGNAL));
			LOGGER.debug("detectConsecutiveDownWeeks - 4 consecutive weeks down");
		}
		else {
			if (downCnt == 5) {
				price = priceSvc.findStockPrice(firstStat.getPriceId());
				signalSvc.createSignal(new StockSignal(price, DN_4_WEEKS_SIGNAL));
				signalSvc.createSignal(new StockSignal(price, DN_5_WEEKS_SIGNAL));
				LOGGER.debug("detectConsecutiveDownWeeks - 5 consecutive weeks down");
			}
		}
	}
	
	private void detectRailroadTracks(List<StockStatistic> weeklyPriceChgList) {
		StockStatistic currStat = weeklyPriceChgList.get(0), prevStat = weeklyPriceChgList.get(4);
		StockPrice price = null;
		double diff = currStat.getStatisticValue() - prevStat.getStatisticValue();
		if (diff > -.015 && diff < .015) {
			price = priceSvc.findStockPrice(currStat.getPriceId());
			signalSvc.createSignal(new StockSignal(price, RR_TRACKS));
		}
	}

	private void detectTrend(StockAveragePrice avgPrice, String trendDirection) {
 		boolean trend = false;
 		int avgPriceCnt = 0;
 		double ap10Day = 0, ap50Day = 0, ap200Day = 0;
 		int av10Day = 0, av50Day = 0, av200Day = 0;
 		StockPrice price = null;
		for (AveragePrice ap: avgPrice.getAvgList()) {
			switch (ap.getDaysCnt()) {
			case 10:
				ap10Day = ap.getAvgPrice();
				av10Day = ap.getAvgVolume();
				avgPriceCnt++;
				break;
			case 50:
				ap50Day = ap.getAvgPrice();
				av50Day = ap.getAvgVolume();
				avgPriceCnt++;
				break;
			case 200:
				ap200Day = ap.getAvgPrice();
				av200Day = ap.getAvgVolume();
				avgPriceCnt++;
				break;
			}
		}
		if (avgPriceCnt == 3) {
			LOGGER.debug("detectTrend - 10 day={}; 50 day={}; 200 day={}", ap10Day, ap50Day, ap200Day);
			if (trendDirection.equals(PRICE_UPTREND_SIGNAL)) {
 				if (ap10Day > ap50Day && ap50Day > ap200Day) {
 					trend = true;
 				}
 				else {
 					trend = false;
 				}
			}
			else {
 				if (ap10Day < ap50Day && ap50Day < ap200Day) {
 					trend = true;
 				}
 				else {
 					trend = false;
 				}
			}
		}
 		if (trend) {
 			price = priceSvc.findStockPrice(avgPrice.getPriceId());
 			signalSvc.createSignal(new StockSignal(price, trendDirection));
 			LOGGER.debug("detectTrend - created signal {} for {}", trendDirection, avgPrice.getPriceId());
 		}
 		if (trendDirection.equals(PRICE_UPTREND_SIGNAL)) {
 	 		if (av10Day > av50Day && av50Day > av200Day) {
 	 			if (price == null) {
 	 				LOGGER.debug("detectTrend - looking for price: {}", avgPrice.getPriceId());
 	 				try {
 	 	 				price = priceSvc.findStockPrice(avgPrice.getPriceId());
 	 				}
 	 				catch (NoSuchElementException e) {
 	 					LOGGER.error("detectTrend - {} {}", avgPrice.getPriceId(), e.getMessage());
 	 				}
 	 			}
 	 			if (price != null) {
 	 	 			signalSvc.createSignal(new StockSignal(price, VOL_UPTREND_SIGNAL));
 	 			}
 	 		}
 		}
	}

	private void minVolumeSignal(StockAveragePrice avgPrice) {
		List<AveragePrice> avgList = avgPrice.getAvgList();
		LOGGER.debug("minVolumeSignal - get price for {}", avgPrice.getPriceId());
		StockPrice price = priceSvc.findStockPrice(avgPrice.getPriceId());
		for (AveragePrice avg: avgList) {
			if (avg.getDaysCnt().equals(50)) {
				if (avg.getAvgVolume().compareTo(500000) > 0) {
					if (price != null) {
	 	 	 			signalSvc.createSignal(new StockSignal(price, AVG_VOL_500K));
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockStatistic> wkPrcChgList = statSvc.retrieveStatList(tickerSymbol, WEEKLY_CLOSE_STAT);
		LOGGER.info("scan - found {} {} statistics for {}", wkPrcChgList.size(), WEEKLY_CLOSE_STAT, tickerSymbol);
		while (wkPrcChgList.size() > 25) {
			this.detectConsecutiveUpWeeks(wkPrcChgList.subList(0, 25));
			this.detectConsecutiveDownWeeks(wkPrcChgList.subList(0, 25));
			this.detectRailroadTracks(wkPrcChgList.subList(0, 5));
			wkPrcChgList.remove(0);
		}
		List<StockAveragePrice> avgPriceList = avgPriceSvc.findAvgPriceList(tickerSymbol);
		for (StockAveragePrice avgPrice: avgPriceList) {
			this.detectTrend(avgPrice, PRICE_UPTREND_SIGNAL);
			this.detectTrend(avgPrice, PRICE_DNTREND_SIGNAL);
			this.minVolumeSignal(avgPrice);
		}
	}

	
}
