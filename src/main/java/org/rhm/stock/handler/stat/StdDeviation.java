package org.rhm.stock.handler.stat;

import java.util.List;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("stdDeviation")
public class StdDeviation implements StatisticCalculator {

	@Autowired
	private StatisticService statSvc = null;
	private static final String STD_DEV_2WK = "STDDEV2WK";
	public static final String STD_DEV_10WK = "STDDEV10WK";
	
	private Logger logger = LoggerFactory.getLogger(StdDeviation.class);
	
	private void calcStdDeviation(List<StockPrice> priceList, String statType) {
		StockPrice firstPrice = priceList.get(0);
		double stdDev = 0.0, sumPrices = 0.0, mean = 0.0, sumDiffSquared = 0.0;
		for (StockPrice price : priceList) {
			sumPrices += price.getClosePrice().doubleValue();
		}
		mean = sumPrices / priceList.size();
		logger.debug("calcStdDeviation - " + firstPrice.getTickerSymbol() + "| Mean: " + mean);
		for (StockPrice price : priceList) {
			sumDiffSquared += Math.pow((price.getClosePrice().doubleValue() - mean), 2);
		}
		stdDev = Math.sqrt(sumDiffSquared / (priceList.size()-1));
		logger.debug("calcStdDeviation - " + firstPrice.getTickerSymbol() + "| Standard deviation: " + stdDev);
		statSvc.createStatistic(
			new StockStatistic(firstPrice.getPriceId(), statType, stdDev, firstPrice.getTickerSymbol(), firstPrice.getPriceDate())
			,false);
	}
	
	@Override
	public void calculate(List<StockPrice> priceList) {
		logger.info("calculate - processing " + priceList.size() + " prices for " + priceList.get(0).getTickerSymbol());
		while (priceList.size() > 50) {
			this.calcStdDeviation(priceList.subList(0, 9), STD_DEV_2WK);
			this.calcStdDeviation(priceList.subList(0, 49), STD_DEV_10WK);
			priceList.remove(0);
		}
	}

}
