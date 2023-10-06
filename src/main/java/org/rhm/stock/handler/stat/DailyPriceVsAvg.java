package org.rhm.stock.handler.stat;

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
@Qualifier("dailyPriceVsAvg")
public class DailyPriceVsAvg implements StatisticCalculator {

	private static final String DLY_PRC_VS_20_DAY_AVG = "DYPRCV20A";
	private static final String DLY_PRC_VS_50_DAY_AVG = "DYPRCV50A";
	private static final String DLY_PRC_VS_200_DAY_AVG = "DYPRCV200A";
	private static final String DLY_VOL_VS_20_DAY_AVG = "DYVOLV20A";
	private static final String DLY_VOL_VS_50_DAY_AVG = "DYVOLV50A";
	private static final String DLY_VOL_VS_200_DAY_AVG = "DYVOLV200A";
	private static final String NET_ABV_BLW_50_DAY_AVG = "NETABVBLW50";
	public static final String AVG_PRC_20_VS_200 = "AVG20V200";

	@Autowired
	private AveragePriceService avgSvc = null;
	@Autowired
	private StatisticService statSvc = null;
	
	private List<StockAveragePrice> avgPriceList = null;
	private Logger logger = LoggerFactory.getLogger(DailyPriceVsAvg.class);
	
	private void init(String tickerSymbol) {
		avgPriceList = avgSvc.findAvgPriceList(tickerSymbol);
		logger.debug("init - found " + avgPriceList.size() + " for " + tickerSymbol );
	}
	
	private StockAveragePrice findStockAvgPrice(String priceId) {
		StockAveragePrice sap = null;
		for (StockAveragePrice stockAvgPrice: this.avgPriceList) {
			if (stockAvgPrice.getPriceId().equals(priceId)) {
				sap = stockAvgPrice;
				logger.debug("findStockAvgPrice - found average price for " + priceId);
				break;
			}
		}
		return sap;
	}
	
	private AveragePrice findAvgPrice(String priceId, int days) {
		StockAveragePrice avg = findStockAvgPrice(priceId);
		AveragePrice avgPrice = null;
		if (avg != null) {
			for (AveragePrice ap: avg.getAvgList()) {
				if (ap.getDaysCnt().intValue() == days) {
					avgPrice = ap;
					break;
				}
			}
		}
		else {
			logger.debug("findAvgPrice - average price not found for " + priceId);
		}
		return avgPrice;
	}
	
	private void calcCurrVsAvg(StockPrice price, int days, String priceType, String volType) {
		AveragePrice avgPrice = this.findAvgPrice(price.getPriceId(), days);
		calcPriceVsAvg(price, days, priceType, avgPrice);
		calcVolVsAvg(price, days, volType, avgPrice);
	}
	
	private void calcPriceVsAvg(StockPrice price, int days, String statType, AveragePrice avgPrice) {
		if (avgPrice != null) {
			logger.debug("calcPriceVsAvg - average price " + avgPrice.getDaysCnt() + ":" + avgPrice.getAvgPrice());
			double priceVsAvg = (price.getClosePrice().doubleValue() / avgPrice.getAvgPrice().doubleValue());
			logger.debug("calcPriceVsAvg - " + price.getPriceId() + " price vs. " + days + " day average=" + priceVsAvg);
			statSvc.createStatistic(
				new StockStatistic(price.getPriceId(), statType, priceVsAvg, price.getTickerSymbol(), price.getPriceDate())
				,false);
		}
		else {
			logger.debug("calcPriceVsAvg - unable to find " + days + " average price for " + price.getPriceId());
		}
	}
	
	private void calcVolVsAvg(StockPrice price, int days, String statType, AveragePrice avgPrice) {
		if (avgPrice != null) {
			logger.debug("calcVolVsAvg - average volume " + avgPrice.getDaysCnt() + ":" + avgPrice.getAvgVolume());
			double volVsAvg = (price.getVolume().doubleValue() / avgPrice.getAvgVolume().doubleValue());
			logger.debug("calcVolVsAvg - " + price.getPriceId() + " volume vs. " + days + " day average=" + volVsAvg);
			statSvc.createStatistic(
				new StockStatistic(price.getPriceId(), statType, volVsAvg, price.getTickerSymbol(), price.getPriceDate())
				,false);
		}
		else {
			logger.debug("calcVolVsAvg - unable to find " + days + " average volume for " + price.getPriceId());
		}
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
				avg20Vs200 = avgPrice20.getAvgPrice().doubleValue() / avgPrice200.getAvgPrice().doubleValue();
				statSvc.createStatistic(
					new StockStatistic(price.getPriceId(), AVG_PRC_20_VS_200, avg20Vs200, price.getTickerSymbol(), price.getPriceDate())
					,false);
			}
			else {
				if (avgPrice200 == null) {
					logger.debug("calcAvg20Vs200 - 200-day avg price not available; cannot calculate " + AVG_PRC_20_VS_200 + " for " + price.getPriceId()); 
				}
			}
		}
	}
	
	private void netAvbBlwStat(List<StockStatistic> statList) {
		double daysAbove = 0, daysBelow = 0;
		StockStatistic firstStat = statList.get(0);
		for (StockStatistic stat: statList) {
			if (stat.getStatisticValue().doubleValue() < 1) {
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
		logger.info("calculate - processing " + priceList.size() + " prices for " + firstPrice.getTickerSymbol());
		for (StockPrice price: priceList) {
			this.calcCurrVsAvg(price, 20, DLY_PRC_VS_20_DAY_AVG, DLY_VOL_VS_20_DAY_AVG);
			this.calcCurrVsAvg(price, 50, DLY_PRC_VS_50_DAY_AVG, DLY_VOL_VS_50_DAY_AVG);
			this.calcCurrVsAvg(price, 200, DLY_PRC_VS_200_DAY_AVG, DLY_VOL_VS_200_DAY_AVG);
			this.calcAvg20Vs200(price);
		}
		this.calcNetAbvBlw(firstPrice.getTickerSymbol());
	}

}
