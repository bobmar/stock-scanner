package org.rhm.stock.repository;

import java.util.Date;

import org.rhm.stock.domain.YahooKeyStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface YahooKeyStatisticRepo extends MongoRepository<YahooKeyStatistic, String> {
	public int deleteByCreateDateBefore(Date priceDate);
	public int deleteByTickerSymbol(String tickerSymbol);
}
