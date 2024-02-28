package org.rhm.stock.handler;

import java.util.ArrayList;
import java.util.List;

import org.rhm.stock.domain.AveragePrice;
import org.rhm.stock.domain.StockPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvgPriceFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(AvgPriceFactory.class);
	
	public static List<AveragePrice> calculateAvgPrices(List<StockPrice> priceList, int[] days) {
		List<AveragePrice> avgPriceList = new ArrayList<AveragePrice>();
		AveragePrice avgPrice = null;
		for (int dayCnt: days) {
			avgPrice = calculateAvgPrice(priceList, dayCnt);
			if (avgPrice != null) {
				avgPriceList.add(avgPrice);
			}
		}
		return avgPriceList;
	}

	public static AveragePrice calculateAvgPrice(List<StockPrice> priceList, int days) {
		LOGGER.info("calculateAvgPrice days={}", days);
		AveragePrice avgPrice = null;
		long totalVolume = 0;
		double totalPrice = 0.0, totalHighLowRange = 0.0, totalOpenCloseRange = 0.0;
		int priceCnt = 0;
		if (priceList.size() > days) {
			avgPrice = new AveragePrice();
			avgPrice.setDaysCnt(days);
			for (StockPrice price: priceList) {
				totalVolume += price.getVolume();
				totalPrice += price.getClosePrice();
				totalHighLowRange += price.getHighLowRange();
				totalOpenCloseRange += price.getOpenCloseRange();
				if (++priceCnt >= days) {
					break;
				}
			}
			avgPrice.setAvgPrice(totalPrice / days);
			avgPrice.setAvgVolume((int)(totalVolume / days));
			avgPrice.setAvgHighLowRange(totalHighLowRange / days);
			avgPrice.setAvgOpenCloseRange(totalOpenCloseRange / days);
		}
		else {
			LOGGER.debug("calculateAvgPrice - insufficient price history to calculate " + days + " day average");
		}
		return avgPrice;
	}
}
