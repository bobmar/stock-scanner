package org.rhm.stock.handler.stat;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("stdDeviation")
public class StdDeviation implements StatisticCalculator {

	@Autowired
	private StatisticService statSvc;
	private static final String STD_DEV_2WK = "STDDEV2WK";
	public static final String STD_DEV_10WK = "STDDEV10WK";
	
	private final Logger LOGGER = LoggerFactory.getLogger(StdDeviation.class);
	
	private void calcStdDeviation(List<StockPrice> priceList, String statType) {
		StockPrice firstPrice = priceList.get(0);
		double stdDev = 0.0, sumPrices = 0.0, mean = 0.0, sumDiffSquared = 0.0;
		for (StockPrice price : priceList) {
			sumPrices += price.getClosePrice();
		}
		mean = sumPrices / priceList.size();
		LOGGER.debug("calcStdDeviation - {}| Mean: {}", firstPrice.getTickerSymbol(), mean);
		for (StockPrice price : priceList) {
			sumDiffSquared += Math.pow((price.getClosePrice() - mean), 2);
		}
		stdDev = Math.sqrt(sumDiffSquared / (priceList.size()-1));
		LOGGER.debug("calcStdDeviation - {}| Standard deviation: {}", firstPrice.getTickerSymbol(), stdDev);
		statSvc.createStatistic(
			new StockStatistic(firstPrice.getPriceId(), statType, stdDev, firstPrice.getTickerSymbol(), firstPrice.getPriceDate())
			,false);
	}
	
	@Override
	public void calculate(List<StockPrice> priceList) {
		LOGGER.info("calculate - processing {} prices for {}", priceList.size(), priceList.get(0).getTickerSymbol());
		while (priceList.size() > 50) {
			this.calcStdDeviation(priceList.subList(0, 9), STD_DEV_2WK);
			this.calcStdDeviation(priceList.subList(0, 49), STD_DEV_10WK);
			priceList.remove(0);
		}
	}

}
