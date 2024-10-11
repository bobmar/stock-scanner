package org.rhm.stock.service;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.dto.CompositePrice;
import org.rhm.stock.dto.PriceBean;
import org.rhm.stock.io.DataDownload;
import org.rhm.stock.repository.PriceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceService {
	@Autowired
	private PriceRepo priceRepo;
	@Autowired
	private CompositePriceService cpSvc;
	@Autowired
	@Qualifier("fmpDownload")
	private DataDownload priceDownload;
	private final static Logger LOGGER = LoggerFactory.getLogger(PriceService.class);
	private final DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd");
	
	public List<StockPrice> retrieveSourcePrices(String tickerSymbol, int days) {
		List<PriceBean> priceBeanList =
				this.priceDownload.downloadPrices(tickerSymbol, days);
		List<StockPrice> priceList = new ArrayList<StockPrice>();
		LOGGER.debug("retrieveSourcePrices - transforming price beans");
		for (PriceBean bean: priceBeanList) {
			StockPrice price = new StockPrice();
			price.setPriceId(String.format("%s:%s", tickerSymbol, dtFmt.format(bean.getDate())));
			price.setClosePrice(bean.getClosePrice());
			price.setHighPrice(bean.getHighPrice());
			price.setLowPrice(bean.getLowPrice());
			price.setOpenPrice(bean.getOpenPrice());
			price.setPriceDate(bean.getDate());
			price.setTickerSymbol(tickerSymbol);
			price.setVolume(bean.getVolume());
			priceList.add(price);
			if (priceList.size() >= days) {
				break;
			}
		}
		LOGGER.debug("retrieveSourcePrices - returning {} StockPrice entries", priceList.size());
		return priceList;
	}
	
	public List<StockPrice> retrievePrices(String tickerSymbol) {
		List<StockPrice> priceList = priceRepo.findByTickerSymbol(tickerSymbol);
		return priceList.stream()
				.sorted((o1,o2) -> {return o1.getPriceDate().compareTo(o2.getPriceDate()) * -1;})
				.collect(Collectors.toList());
	}
	
	public StockPrice saveStockPrice(StockPrice price) {
		StockPrice savedPrice = priceRepo.save(price);
		return savedPrice;
	}
	
	public List<StockPrice> saveStockPrice(List<StockPrice> priceList) {
		List<StockPrice> savedPrices = priceRepo.saveAll(priceList);
		LOGGER.debug("saveStockPrice(List) - saved " + priceList.size() + " prices");
		return savedPrices;
	}
	
	public CompositePrice retrieveCurrentPrice(String tickerSymbol) {
		StockPrice price = priceRepo.findTopByTickerSymbolOrderByPriceDateDesc(tickerSymbol);
		CompositePrice cPrice = null;
		if (price != null) {
			cPrice = cpSvc.compositePriceFactory(price.getPriceId());
		}
		return cPrice;
	}
	
	public StockPrice findStockPrice(String priceId) {
		Optional<StockPrice> opt = priceRepo.findById(priceId);
		return opt.isPresent()?opt.get():null;
	}
	
	public StockPrice findLatestStockPrice(String tickerSymbol) {
		StockPrice price = priceRepo.findTopByTickerSymbolOrderByPriceDateDesc(tickerSymbol);
		return price;
	}
	
	public long deleteOlderThan(Date deleteBefore) {
//		return priceRepo.deleteOlderThan(deleteBefore);
		return priceRepo.deleteByPriceDateBefore(deleteBefore);
	}
	
	public long deleteByTickerSymbol(String tickerSymbol) { 
		return priceRepo.deleteByTickerSymbol(tickerSymbol);
	}
	
	public List<StockPrice> findByTickerAndPriceDate(String tickerSymbol, Date priceDate) {
		return priceRepo.findTop30ByTickerSymbolAndPriceDateGreaterThanOrderByPriceDateDesc(tickerSymbol, priceDate);
	}
	
	public long priceCount(String tickerSymbol) {
		StockPrice price = new StockPrice();
		price.setTickerSymbol(tickerSymbol);
		Example<StockPrice> example = Example.of(price);
		return priceRepo.count(example);
	}
	
	public List<String> findPriceTickers(Date priceDate) {
		List<String> tickerList = priceRepo.findUniqueTickerSymbols(priceDate);
		return tickerList;
	}
	
}
