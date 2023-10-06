package org.rhm.stock.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.repository.AveragePriceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AveragePriceService {
	@Autowired
	private AveragePriceRepo avgPriceRepo = null;
	
	public StockAveragePrice createAveragePrice(StockAveragePrice avgPrice) {
		return avgPriceRepo.save(avgPrice);
	}
	
	public StockAveragePrice findAvgPrice(String priceId) {
		Optional<StockAveragePrice> avgPriceResult = avgPriceRepo.findById(priceId);
		StockAveragePrice avgPrice = null;
		if (avgPriceResult.isPresent()) {
			avgPrice = avgPriceResult.get();
		}
		return avgPrice;
	}
	
	public List<StockAveragePrice> findAvgPriceList(String tickerSymbol) {
		List<StockAveragePrice> avgPriceList = null;
		avgPriceList = avgPriceRepo.findByTickerSymbol(tickerSymbol)
				.stream()
				.sorted((o1,o2)->{return o1.getPriceId().compareTo(o2.getPriceId()) * -1;})
				.collect(Collectors.toList());
		return avgPriceList;
	}
	
	public List<StockAveragePrice> findRecentAvgPriceList(String tickerSymbol) {
		List<StockAveragePrice> avgPriceList = null;
		avgPriceList = avgPriceRepo.findTop10ByTickerSymbolOrderByPriceDateDesc(tickerSymbol)
				.stream()
				.sorted((o1,o2)->{return o1.getPriceId().compareTo(o2.getPriceId()) * -1;})
				.collect(Collectors.toList());
		return avgPriceList;
	}
	
	public long deleteOlderThan(Date deleteBefore) {
//		return avgPriceRepo.deleteOlderThan(deleteBefore);
		return avgPriceRepo.deleteByPriceDateBefore(deleteBefore);
	}
	
	public long deleteByTickerSymbol(String tickerSymbol) {
		return avgPriceRepo.deleteByTickerSymbol(tickerSymbol);
	}
	
	public List<String> findAvgPriceTickers(Date priceDate) {
		List<String> tickerSymbolList = avgPriceRepo.findUniqueTickerSymbols(priceDate);
		return tickerSymbolList;
	}
}
