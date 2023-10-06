package org.rhm.stock.repository;

import java.util.Date;
import java.util.List;

import org.rhm.stock.domain.StockStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticRepo extends MongoRepository<StockStatistic, String>, StatisticCustomRepo {
	public List<StockStatistic> findByTickerSymbol(String tickerSymbol);
	public List<StockStatistic> findByTickerSymbolAndStatisticType(String tickerSymbol, String statisticType);
	public List<StockStatistic> findByPriceId(String priceId);
	public List<StockStatistic> findByStatisticTypeAndPriceDate(String statisticType, Date priceDate);
	public List<StockStatistic> findByTickerSymbolAndStatisticTypeAndPriceDate(String tickerSymbol, String statisticType, Date priceDate);
	public StockStatistic findTopByOrderByPriceDateDesc();
	public List<StockStatistic> findByPriceDateGreaterThan(Date priceDate);
	public int deleteByTickerSymbol(String tickerSymbol);
	public int deleteByPriceDateBefore(Date priceDate);
}
