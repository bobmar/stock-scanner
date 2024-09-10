package org.rhm.stock.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.rhm.stock.domain.AveragePrice;
import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.io.CompanyInfoDownload;
import org.rhm.stock.repository.AveragePriceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AveragePriceService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AveragePriceService.class);
	@Autowired
	private AveragePriceRepo avgPriceRepo = null;
	@Autowired
	private CompanyInfoDownload coInfo;
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

	public void saveAll(List<StockAveragePrice> averagePrices) {
		avgPriceRepo.saveAll(averagePrices);
	}
	public long deleteOlderThan(Date deleteBefore) {
		return avgPriceRepo.deleteByPriceDateBefore(deleteBefore);
	}
	
	public long deleteByTickerSymbol(String tickerSymbol) {
		return avgPriceRepo.deleteByTickerSymbol(tickerSymbol);
	}
	
	public List<String> findAvgPriceTickers(Date priceDate) {
		return avgPriceRepo.findUniqueTickerSymbols(priceDate);
	}

	public Map<String,Object> retrieveEma(String tickerSymbol, String period, String earliestDate) {
		Map<String,Object> emaPriceMap = new HashMap<>();
		List<Map<String,Object>> emaList = this.coInfo.retrieveEma(tickerSymbol, period);
		if (null != emaList) {
			for (Map<String,Object> emaEntry: emaList) {
				String emaDate = ((String)emaEntry.get("date")).substring(0,10);
				if (emaDate.compareTo(earliestDate) >= 0) {
					emaPriceMap.put(emaDate, emaEntry);
				}
			}
		}
		else {
			LOGGER.warn("retrieveEma - emaList for {} of period {} is null", tickerSymbol, period);
		}
		return emaPriceMap;
	}

	public List<StockAveragePrice> addEmaToAvgBal(String tickerSymbol) {
		DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd");
		List<StockAveragePrice> averagePrices = this.findAvgPriceList(tickerSymbol);
		String earliestDate = dtFmt.format(averagePrices.get(averagePrices.size()-1).getPriceDate());
		int[] period = {10, 20, 50, 200};
		for (int p: period) {
			Map<String,Object> emaMap = this.retrieveEma(tickerSymbol, String.valueOf(p), earliestDate);
			if (null != emaMap) {
				LOGGER.info("addEmaToAvgBal - found {} {}-day EMA entries for {}", emaMap.size(), p, tickerSymbol);
				for (StockAveragePrice avgPrice: averagePrices) {
					AveragePrice periodAvg = avgPrice.findAvgPrice(p);
					String priceDate = dtFmt.format(avgPrice.getPriceDate());
					if (null != periodAvg) {
						Map<String,Object> emaItem = (Map<String,Object>)emaMap.get(priceDate);
						if (null != emaItem) {
							periodAvg.setEmaPrice((Double)emaItem.get("ema"));
						}
						else {
							LOGGER.warn("addEmaToAvgBal - unable to find {} {}-day EMA on {}", tickerSymbol, p, priceDate);
						}
					}
				}
			}
		}
		return averagePrices;
	}
}
