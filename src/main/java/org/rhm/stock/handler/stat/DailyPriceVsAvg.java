package org.rhm.stock.handler.stat;

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

import java.util.List;

@Component
@Qualifier("dailyPriceVsAvg")
public class DailyPriceVsAvg implements StatisticCalculator {

	private static final String DLY_PRC_VS_10_DAY_AVG = "DYPRCV10A";
	private static final String DLY_PRC_VS_20_DAY_AVG = "DYPRCV20A";
	private static final String DLY_PRC_VS_50_DAY_AVG = "DYPRCV50A";
	private static final String DLY_PRC_VS_200_DAY_AVG = "DYPRCV200A";
	private static final String DLY_PRC_VS_10_DAY_EMA = "DYPRCV10E";
	private static final String DLY_PRC_VS_20_DAY_EMA = "DYPRCV20E";
	private static final String DLY_PRC_VS_50_DAY_EMA = "DYPRCV50E";
	private static final String DLY_PRC_VS_200_DAY_EMA = "DYPRCV200E";
	private static final String DLY_VOL_VS_10_DAY_AVG = "DYVOLV10A";
	private static final String DLY_VOL_VS_20_DAY_AVG = "DYVOLV20A";
	private static final String DLY_VOL_VS_50_DAY_AVG = "DYVOLV50A";
	private static final String DLY_VOL_VS_200_DAY_AVG = "DYVOLV200A";
	private static final String NET_ABV_BLW_50_DAY_AVG = "NETABVBLW50";
	public static final String AVG_PRC_20_VS_200 = "AVG20V200";

	@Autowired
	private AveragePriceService avgSvc;
	@Autowired
	private StatisticService statSvc;
	
	private List<StockAveragePrice> avgPriceList = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(DailyPriceVsAvg.class);
	
	private void init(String tickerSymbol) {
		avgPriceList = avgSvc.findAvgPriceList(tickerSymbol);
		LOGGER.debug("init - found {} for {}", avgPriceList.size(), tickerSymbol );
	}
	
	private StockAveragePrice findStockAvgPrice(String priceId) {
		StockAveragePrice sap = null;
		for (StockAveragePrice stockAvgPrice: this.avgPriceList) {
			if (stockAvgPrice.getPriceId().equals(priceId)) {
				sap = stockAvgPrice;
				LOGGER.debug("findStockAvgPrice - found average price for {}", priceId);
				break;
			}
		}
		return sap;
	}
	
	private AveragePrice findAvgPrice(String priceId, int days) {
		StockAveragePrice avg = findStockAvgPrice(priceId);
		AveragePrice avgPrice = null;
		if (avg != null) {
			avgPrice = avg.findAvgPrice(days);
		}
		return avgPrice;
	}
	
	private void calcCurrVsAvg(StockPrice price, int days, String priceType, String volType) {
		AveragePrice avgPrice = this.findAvgPrice(price.getPriceId(), days);
		calcPriceVsAvg(price, days, priceType, avgPrice);
		calcVolVsAvg(price, days, volType, avgPrice);
	}

	private void calcCurrVsEma(StockPrice price, int days, String statType) {
		StockStatistic stat = null;
		AveragePrice avgPrice = this.findAvgPrice(price.getPriceId(), days);
		if (avgPrice != null) {
			if (avgPrice.getEmaPrice() != null) {
				double priceVsEma = (price.getClosePrice() / avgPrice.getEmaPrice());
				stat = new StockStatistic(price.getPriceId(), statType, priceVsEma, price.getTickerSymbol(), price.getPriceDate());
				statSvc.createStatistic(stat,false);
			}
			else {
				LOGGER.warn("calcCurrVsEma - no {} day EMA found for {}", days, price.getPriceId());
			}
		}
	}

	protected StockStatistic calcPriceVsAvg(StockPrice price, int days, String statType, AveragePrice avgPrice) {
		StockStatistic stat = null;
		if (avgPrice != null) {
			LOGGER.debug("calcPriceVsAvg - average price {}:{}", avgPrice.getDaysCnt(), avgPrice.getAvgPrice());
			double priceVsAvg = (price.getClosePrice() / avgPrice.getAvgPrice());
			LOGGER.debug("calcPriceVsAvg - {} price vs. {} day average={}", price.getPriceId(), days, priceVsAvg);
			stat = new StockStatistic(price.getPriceId(), statType, priceVsAvg, price.getTickerSymbol(), price.getPriceDate());
			statSvc.createStatistic(stat,false);
		}
		else {
			LOGGER.debug("calcPriceVsAvg - unable to find {} average price for {}", days, price.getPriceId());
		}
		return stat;
	}
	
	protected StockStatistic calcVolVsAvg(StockPrice price, int days, String statType, AveragePrice avgPrice) {
		StockStatistic stat = null;
		if (avgPrice != null) {
			LOGGER.debug("calcVolVsAvg - average volume {}:{}", avgPrice.getDaysCnt(), avgPrice.getAvgVolume());
			double volVsAvg = (price.getVolume().doubleValue() / avgPrice.getAvgVolume().doubleValue());
			LOGGER.debug("calcVolVsAvg - {} volume vs. {} day average={}", price.getPriceId(), days, volVsAvg);
			stat = new StockStatistic(price.getPriceId(), statType, volVsAvg, price.getTickerSymbol(), price.getPriceDate());
			statSvc.createStatistic(stat,false);
		}
		else {
			LOGGER.debug("calcVolVsAvg - unable to find {} average volume for {}", days, price.getPriceId());
		}
		return stat;
	}

	private void calcAvg20Vs200(StockPrice price) {
		StockAveragePrice avgPrice = this.findStockAvgPrice(price.getPriceId());
		if (avgPrice != null) {
			AveragePrice avgPrice20 = null, avgPrice200 = null;
			for (AveragePrice ap: avgPrice.getAvgList()) {
				switch (ap.getDaysCnt()) {
				case 20:
					avgPrice20 = ap;
					break;
				case 200:
					avgPrice200 = ap;
					break;
				}
			}
			double avg20Vs200 = 0.0;
			if (avgPrice20 != null && avgPrice200 != null) {
				avg20Vs200 = avgPrice20.getAvgPrice() / avgPrice200.getAvgPrice();
				statSvc.createStatistic(
					new StockStatistic(price.getPriceId(), AVG_PRC_20_VS_200, avg20Vs200, price.getTickerSymbol(), price.getPriceDate())
					,false);
			}
			else {
				if (avgPrice200 == null) {
					LOGGER.debug("calcAvg20Vs200 - 200-day avg price not available; cannot calculate {} for {}", AVG_PRC_20_VS_200, price.getPriceId());
				}
			}
		}
	}
	
	private void netAvbBlwStat(List<StockStatistic> statList) {
		double daysAbove = 0, daysBelow = 0;
		StockStatistic firstStat = statList.get(0);
		for (StockStatistic stat: statList) {
			if (stat.getStatisticValue() < 1) {
				daysBelow++;
			}
			else {
				daysAbove++;
			}
		}
		statSvc.createStatistic(new StockStatistic(
			firstStat.getPriceId()
			, NET_ABV_BLW_50_DAY_AVG
			, daysAbove / (daysAbove + daysBelow)
			, firstStat.getTickerSymbol()
			, firstStat.getPriceDate())
		);
	}
	
	private void calcNetAbvBlw(String tickerSymbol) {
		List<StockStatistic> statList = statSvc.retrieveStatList(tickerSymbol, DLY_PRC_VS_50_DAY_AVG);
		while (statList.size() > 50) {
			this.netAvbBlwStat(statList.subList(0, 50));
			statList.remove(0);
		}
	}
	
	@Override
	public void calculate(List<StockPrice> priceList) {
		StockPrice firstPrice = priceList.get(0);
		this.init(firstPrice.getTickerSymbol());
		LOGGER.info("calculate - processing {} prices for {}", priceList.size(), firstPrice.getTickerSymbol());
		for (StockPrice price: priceList) {
			this.calcCurrVsAvg(price, 10, DLY_PRC_VS_10_DAY_AVG, DLY_VOL_VS_10_DAY_AVG);
			this.calcCurrVsAvg(price, 20, DLY_PRC_VS_20_DAY_AVG, DLY_VOL_VS_20_DAY_AVG);
			this.calcCurrVsAvg(price, 50, DLY_PRC_VS_50_DAY_AVG, DLY_VOL_VS_50_DAY_AVG);
			this.calcCurrVsAvg(price, 200, DLY_PRC_VS_200_DAY_AVG, DLY_VOL_VS_200_DAY_AVG);
			this.calcCurrVsEma(price, 10, DLY_PRC_VS_10_DAY_EMA);
			this.calcCurrVsEma(price, 20, DLY_PRC_VS_20_DAY_EMA);
			this.calcCurrVsEma(price, 50, DLY_PRC_VS_50_DAY_EMA);
			this.calcCurrVsEma(price, 200, DLY_PRC_VS_200_DAY_EMA);
			this.calcAvg20Vs200(price);
		}
		this.calcNetAbvBlw(firstPrice.getTickerSymbol());
	}

}
