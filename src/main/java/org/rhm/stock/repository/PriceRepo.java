package org.rhm.stock.repository;

import java.util.Date;
import java.util.List;

import org.rhm.stock.domain.StockPrice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepo extends MongoRepository<StockPrice, String>, PriceCustomRepo {
	public List<StockPrice> findByTickerSymbol(String tickerSymbol);
	public StockPrice findTopByTickerSymbolOrderByPriceDateDesc(String tickerSymbol);
	public List<StockPrice> findTop60ByTickerSymbolOrderByPriceDateDesc(String tickerSymbol);
	public List<StockPrice> findTop30ByTickerSymbolAndPriceDateGreaterThanOrderByPriceDateDesc(String tickerSymbol, Date priceDate);
	public List<StockPrice> findByPriceDateGreaterThan(Date priceDate);
	public List<StockPrice> findByTickerSymbolAndPriceDateGreaterThan(String tickerSymbol, Date priceDate);
	public int deleteByTickerSymbol(String tickerSymbol);
	public int deleteByPriceDateBefore(Date priceDate);
}
