package org.rhm.stock.repository;

import java.util.Date;
import java.util.List;

import org.rhm.stock.domain.StockAveragePrice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AveragePriceRepo extends MongoRepository<StockAveragePrice, String>, AveragePriceCustomRepo {

	public List<StockAveragePrice> findByTickerSymbol(String tickerSymbol);
	public List<StockAveragePrice> findTop10ByTickerSymbolOrderByPriceDateDesc(String tickerSymbol);
	public List<StockAveragePrice> findByPriceDate(Date priceDate);
	public List<StockAveragePrice> findByPriceId(String priceId);
	public List<StockAveragePrice> findByPriceDateGreaterThan(Date priceDate);
	public int deleteByTickerSymbol(String tickerSymbol);
	public int deleteByPriceDateBefore(Date priceDate);
}
