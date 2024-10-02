package org.rhm.stock.handler.stat;

import java.util.ArrayList;
import java.util.List;

import org.rhm.stock.domain.AveragePrice;
import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.service.AveragePriceService;
import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("momentum")
public class Momentum implements StatisticCalculator {
	@Autowired
	private StatisticService statSvc;
	@Autowired
	private AveragePriceService avgPriceSvc;

	private static final String STAT_Z_SCORE = "ZSCORE";
	private static final String STAT_TR_MOM = "TRMOM";
	private List<StockAveragePrice> avgPriceList;
	private List<StockStatistic> statList;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(Momentum.class);
	
	private void init(String tickerSymbol) {
		avgPriceList = avgPriceSvc.findAvgPriceList(tickerSymbol);
		statList = statSvc.retrieveStatList(tickerSymbol, StdDeviation.STD_DEV_10WK);
	}
	
	private void calcTotalReturnMomentum(List<StockPrice> priceList) {
		StockPrice todayPrice = null, oldPrice = null;
		todayPrice = priceList.get(0);
		double trMomentum = 0.0;
		if (priceList.size() >= 50) {
			oldPrice = priceList.get(49);
			if (todayPrice != null && oldPrice != null) {
				trMomentum = ((todayPrice.getClosePrice() / oldPrice.getClosePrice()) - 1);
				statSvc.createStatistic(
					new StockStatistic(todayPrice.getPriceId(), STAT_TR_MOM, trMomentum, todayPrice.getTickerSymbol(), todayPrice.getPriceDate()));
			}
		}
	}
	
	private StockAveragePrice findAvgPrice(String priceId) {
		StockAveragePrice avgPrice = null;
		for (StockAveragePrice avg: avgPriceList) {
			if (avg.getPriceId().equals(priceId)) {
				avgPrice = avg;
				break;
			}
		}
		return avgPrice;
	}
	
	private StockStatistic findStdDev(String priceId) {
		StockStatistic stdDev = null;
		for (StockStatistic stat: statList) {
			if (stat.getPriceId().equals(priceId)) {
				stdDev = stat;
				break;
			}
		}
		return stdDev;
	}
	
	private void calcZScore(List<StockPrice> priceList) {
		StockAveragePrice avgPrice = null;
		AveragePrice avg50Day = null;
		StockStatistic stdDev = null;
		double zScore = 0.0;
		for (StockPrice price: priceList) {
			avgPrice = this.findAvgPrice(price.getPriceId());
			if (avgPrice != null) {
				for (AveragePrice avg: avgPrice.getAvgList()) {
					if (avg.getDaysCnt() == 50) {
						avg50Day = avg;
						break;
					}
				}
				stdDev = this.findStdDev(price.getPriceId());
				if (avg50Day != null && (stdDev != null && stdDev.getStatisticValue() > 0)) {
					LOGGER.debug("calcZScore - priceID={} 50-Day avg price={}; closing price={}; std dev={}", price.getPriceId(), avg50Day.getAvgPrice(), price.getClosePrice(), stdDev.getStatisticValue());
					zScore = ((price.getClosePrice() - avg50Day.getAvgPrice())
						/ stdDev.getStatisticValue());
					statSvc.createStatistic(
						new StockStatistic(price.getPriceId(), STAT_Z_SCORE, zScore, price.getTickerSymbol(), price.getPriceDate()));
				}
				else {
					LOGGER.warn("calcZScore - unable to find standard deviation for {}", price.getPriceId());
				}
			}
			else {
				LOGGER.warn("calcZScore - average price record for {} is not found", price.getPriceId());
			}
		}
	}
	
	@Override
	public void calculate(List<StockPrice> priceList) {
		List<StockPrice> workList = new ArrayList<StockPrice>();
		LOGGER.info("calculate - processing " + priceList.size() + " prices for " + priceList.get(0).getTickerSymbol());
		this.init(priceList.get(0).getTickerSymbol());
		this.calcZScore(priceList);
		workList.addAll(priceList);
		while (workList.size() > 50) {
			this.calcTotalReturnMomentum(workList);
			workList.remove(0);
		}

	}

}
