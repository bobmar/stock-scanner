package org.rhm.stock.service;

import org.rhm.stock.domain.YahooKeyStatistic;
import org.rhm.stock.repository.YahooKeyStatisticRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Deprecated
@Component
public class KeyStatService {
	@Autowired
	private YahooKeyStatisticRepo repo = null;
	
	public YahooKeyStatistic saveStatistic(YahooKeyStatistic keyStat) {
		return repo.save(keyStat);
	}
	
	public int deleteOlderThan(Date deleteBefore) {
		return repo.deleteByCreateDateBefore(deleteBefore);
	}
	
	public int deleteByTickerSymbol(String tickerSymbol) {
		return repo.deleteByTickerSymbol(tickerSymbol);
	}
	
	public List<YahooKeyStatistic> retrieveStats() {
		List<YahooKeyStatistic> ksList = repo.findAll();
		return ksList;
	}
}
