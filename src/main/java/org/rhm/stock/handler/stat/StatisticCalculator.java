package org.rhm.stock.handler.stat;

import java.util.List;

import org.rhm.stock.domain.StockPrice;

public interface StatisticCalculator {

	public void calculate(List<StockPrice> priceList);
}
