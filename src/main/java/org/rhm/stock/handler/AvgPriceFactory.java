package org.rhm.stock.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.rhm.stock.domain.AveragePrice;
import org.rhm.stock.domain.StockPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvgPriceFactory {
	private static Logger logger = LoggerFactory.getLogger(AvgPriceFactory.class);
	
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
		AveragePrice avgPrice = null;
		int totalVolume = 0;
		double totalPrice = 0.0, totalHighLowRange = 0.0, totalOpenCloseRange = 0.0;
		int priceCnt = 0;
		if (priceList.size() > days) {
			avgPrice = new AveragePrice();
			avgPrice.setDaysCnt(days);
			for (StockPrice price: priceList) {
				totalVolume += price.getVolume();
				totalPrice += price.getClosePrice().doubleValue();
				totalHighLowRange += price.getHighLowRange().doubleValue();
				totalOpenCloseRange += price.getOpenCloseRange().doubleValue();
				if (++priceCnt > days) {
					break;
				}
			}
			avgPrice.setAvgPrice(totalPrice / days);
			avgPrice.setAvgVolume(totalVolume / days);
			avgPrice.setAvgHighLowRange(totalHighLowRange / days);
			avgPrice.setAvgOpenCloseRange(totalOpenCloseRange / days);
			logger.debug("calculateAvgPrice - " + days + " average price/volume=" + avgPrice.getAvgPrice() + "/" + avgPrice.getAvgVolume());
		}
		else {
			logger.debug("calculateAvgPrice - insufficient price history to calculate " + days + " day average");
		}
		return avgPrice;
	}
}
