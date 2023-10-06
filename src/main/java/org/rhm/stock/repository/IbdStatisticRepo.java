package org.rhm.stock.repository;

import java.util.Date;
import java.util.List;

import org.rhm.stock.domain.IbdStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IbdStatisticRepo extends MongoRepository<IbdStatistic, String> {

	public List<IbdStatistic> findByTickerSymbol(String tickerSymbol);
	public List<IbdStatistic> findByPriceDate(Date priceDate);
	public IbdStatistic findTopByOrderByPriceDateDesc();
	public IbdStatistic findTopByTickerSymbolOrderByPriceDateDesc(String tickerSymbol);
	public List<IbdStatistic> findByOrderByPriceDateDesc();
	public int deleteByPriceDateBefore(Date priceDate);
	public int deleteByTickerSymbol(String tickerSymbol);
}
