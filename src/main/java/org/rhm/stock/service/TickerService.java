package org.rhm.stock.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rhm.stock.controller.dto.TickerInfo;
import org.rhm.stock.domain.IbdStatistic;
import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.dto.FinanceProfile;
import org.rhm.stock.handler.ticker.ExcelTransformer;
import org.rhm.stock.handler.ticker.ExcelTransformerResponse;
import org.rhm.stock.io.CboeWeekly;
import org.rhm.stock.io.YahooDownload;
import org.rhm.stock.repository.IbdStatisticRepo;
import org.rhm.stock.repository.PriceRepo;
import org.rhm.stock.repository.TickerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class TickerService {
	
	@Autowired
	private TickerRepo tickerRepo = null;
	@Autowired
	private YahooDownload download = null;
	@Autowired
	private ExcelTransformer excel = null;
	@Autowired
	private IbdStatisticRepo ibdRepo = null;
	@Autowired
	private PriceRepo priceRepo = null;
	@Autowired
	private CboeWeekly cboeWeekly;
	private Logger logger = LoggerFactory.getLogger(TickerService.class);
	public String createTicker(String tickerSymbol) {
		StockTicker stockTicker = null;
		String message = null;
		Map<String, Object> companyInfo = download.retrieveProfile(tickerSymbol);
		if (companyInfo == null) {
			message = "Unable to find " + tickerSymbol + " in Yahoo Finance";
		}
		else {
			if (tickerRepo.existsById(tickerSymbol)) {
				message = "Ticker " + tickerSymbol + " already exists";
			}
			else {
				FinanceProfile profile = new FinanceProfile(companyInfo);
				stockTicker = new StockTicker();
				stockTicker.setTickerSymbol(tickerSymbol);
				stockTicker.setCompanyName(profile.getLongName());
				stockTicker.setIndustryName(profile.getIndustry());
				stockTicker.setSectorName(profile.getSector());
				if (tickerRepo.insert(stockTicker) != null) {
					message = tickerSymbol + "/" + stockTicker.getCompanyName() + " was successfully created";
				}
				else {
					message = "Failed to create entry for " + tickerSymbol;
				}
			}
		}
		return message;
	}
	@Cacheable("tickerList")
	public List<StockTicker> retrieveTickerList() {
		return tickerRepo.findAll().stream()
				.sorted((o1,o2)->{return o1.getTickerSymbol().compareTo(o2.getTickerSymbol());})
				.collect(Collectors.toList());
	}
	
	public int saveTickerList(List<TickerInfo> tickerList) {
		int tickersSavedCnt = 0;
		String status = null;
		for (TickerInfo info: tickerList) {
			status = this.createTicker(info.getTickerSymbol(), info.getCompanyName());
			if (status.contains("successfully created")) {
				tickersSavedCnt++;
			}
		}
		return tickersSavedCnt;
	}
	
	public String createTicker(String tickerSymbol, String companyName) {
		String message = null;
		StockTicker stockTicker = null;
		if (tickerRepo.existsById(tickerSymbol)) {
			message = "Ticker " + tickerSymbol + " already exists";
		}
		else {
			stockTicker = new StockTicker();
			stockTicker.setTickerSymbol(tickerSymbol);
			stockTicker.setCompanyName(companyName);
			if (tickerRepo.insert(stockTicker) != null) {
				message = tickerSymbol + "/" + stockTicker.getCompanyName() + " was successfully created";
			}
			else {
				message = "Failed to create entry for " + tickerSymbol;
			}
		}
		
		return message;
	}
	
	private String findCompanyName(List<IbdStatistic> ibdStatList, String tickerSymbol) {
		String companyName = null;
		for (IbdStatistic ibdStat: ibdStatList) {
			if (tickerSymbol.equals(ibdStat.getTickerSymbol())) {
				companyName = ibdStat.getCompanyName();
				break;
			}
		}
		return companyName;
	}
	
	public List<TickerInfo> retrieveTickerInfo(byte[] workbookBytes) {
		List<TickerInfo> tickerInfoList = new ArrayList<TickerInfo>();
		ExcelTransformerResponse response = excel.extractTickerSymbols(workbookBytes);
		List<IbdStatistic> ibdStatList = response.getIbdStatList();
		List<String> tickerList = response.getTickerSymbols();
		FinanceProfile profile = null;
		TickerInfo ticker = null;
		for (String tickerSymbol: tickerList) {
			profile = this.findCompanyProfile(tickerSymbol);
			ticker = new TickerInfo();
			ticker.setTickerSymbol(tickerSymbol);
			if (profile != null) {
				ticker.setCompanyName(profile.getLongName());
				if (this.tickerExists(tickerSymbol)) {
					ticker.setStatus("Ticker already exists; will not be created"); 
				}
				else {
					ticker.setStatus("OK");
				}
			}
			else {
				logger.info("retrieveTickerInfo - ticker " + tickerSymbol + " was not found in Yahoo Finance");
				ticker.setCompanyName(this.findCompanyName(ibdStatList, tickerSymbol));
				if (ticker.getCompanyName() != null) {
					if (this.tickerExists(tickerSymbol)) {
						ticker.setStatus("Ticker already exists; will not be created"); 
					}
					else {
						ticker.setStatus("OK");
					}
				}
				else {
					ticker.setStatus("No company name available");
				}
			}
			tickerInfoList.add(ticker);
			logger.info("retrieveTickerInfo - " + ticker.getTickerSymbol() + ":" + ticker.getStatus());
		}
		this.loadIbdStatistics(response.getIbdStatList(), response.getListName());
		return tickerInfoList.stream()
				.sorted((o1,o2)->{return o1.getTickerSymbol().compareTo(o2.getTickerSymbol());})
				.collect(Collectors.toList());
	}
	
	private void loadIbdStatistics(List<IbdStatistic> ibdStatList, String listName) {
		List<StockPrice> priceList = null;
		logger.info(String.format("loadIbdStatistics - loading %s IBD stats", ibdStatList.size()) );
		IbdStatistic mrIbdStat = null;
		for (IbdStatistic ibdStat: ibdStatList) {
			mrIbdStat = ibdRepo.findTopByTickerSymbolOrderByPriceDateDesc(ibdStat.getTickerSymbol());
			ibdStat.getListName().add(listName);
			if (mrIbdStat != null) {
				priceList = priceRepo.findByTickerSymbolAndPriceDateGreaterThan(mrIbdStat.getTickerSymbol(), mrIbdStat.getPriceDate());
				if (priceList != null && priceList.size() > 0) {
					priceList.forEach(price->{
						this.createIbdStat(ibdStat, price.getPriceId(), price.getPriceDate());
					}); 
				}
				else {
					StockPrice price = priceRepo.findTopByTickerSymbolOrderByPriceDateDesc(ibdStat.getTickerSymbol());
					this.createIbdStat(ibdStat, price.getPriceId(), price.getPriceDate());
				}
			}
			else {
				StockPrice price = priceRepo.findTopByTickerSymbolOrderByPriceDateDesc(ibdStat.getTickerSymbol());
				if (price != null) {
					this.createIbdStat(ibdStat, price.getPriceId(), price.getPriceDate());
				}
				else {
					logger.info("loadIbdStatistics - new IBD stat ticker: " + ibdStat.getTickerSymbol());
				}
			}
		}
	}

	private void createIbdStat(IbdStatistic ibdStat, String priceId, Date priceDate) {
		logger.info("createIbdStat - priceId: " + priceId);
		Optional<IbdStatistic> existingStat = ibdRepo.findById(priceId);
		if (existingStat.isPresent()) {
			logger.info("createIbdStat - found existing IBD stat");
			List<String> newListNames = ibdStat.getListName();
			logger.info("createIbdStat - list count: " + newListNames.size());
			for (String list: existingStat.get().getListName()) {
				if (!newListNames.contains(list)) {
					newListNames.add(list);
				}
			}
			logger.info("createIbdStat - list count after adding existing: " + newListNames.size());
			ibdStat.setListName(newListNames);
			if (ibdStat.getListName().size() > 1) {
				logger.info("createIbdStat - " + priceId + " is on multiple IBD lists");
			}
		}
		else {
			logger.info("createIbdStat - no existing IBD stat for " + priceId);
		}
		ibdStat.setStatId(priceId);
		ibdStat.setPriceDate(priceDate);
		ibdRepo.save(ibdStat);
	}
	
	public boolean tickerExists(String tickerSymbol) {
		boolean exists = false;
		exists = tickerRepo.existsById(tickerSymbol);
		return exists;
	}
	
	public FinanceProfile findCompanyProfile(String tickerSymbol) {
//		Map<String,Object> profile = download.retrieveProfile(tickerSymbol);
		FinanceProfile fp = null;
//		if (profile != null) {
//			fp = new FinanceProfile(profile);
//		}
		return fp;
	}

	public Page<StockTicker> findPage(Pageable pageable) {
		Sort sort = Sort.by(Direction.ASC, "tickerSymbol");
		PageRequest pageReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return tickerRepo.findAll(pageReq);
	}
	
	public void deleteTicker(String tickerSymbol) {
		tickerRepo.deleteById(tickerSymbol);
	}
	
	public List<IbdStatistic> findIbdStats(String tickerSymbol) {
		List<IbdStatistic> ibdList = null;
		ibdList = this.ibdRepo.findByTickerSymbol(tickerSymbol);
		return ibdList.stream().sorted((o1,o2)->{return o1.getPriceDate().compareTo(o2.getPriceDate()) * -1;}).collect(Collectors.toList());
	}
	
	public int deleteIbdStatsOlderThan(Date deleteBefore) {
		return ibdRepo.deleteByPriceDateBefore(deleteBefore);
	}
	
	public int deleteIbdStatsByTicker(String tickerSymbol) {
		return ibdRepo.deleteByTickerSymbol(tickerSymbol);
	}

	public int updateWeeklyOptions() {
		List<String> weeklyTickers = cboeWeekly.retrieveWeeklyOptionStocks();
		int weeklyOptionCnt = 0;
		List<StockTicker> tickers = this.retrieveTickerList();
		for (StockTicker ticker: tickers) {
			if (weeklyTickers.contains(ticker.getTickerSymbol())) {
				ticker.setWeeklyOptions(true);
				weeklyOptionCnt++;
			}
			else {
				ticker.setWeeklyOptions(false);
			}
		}
		tickerRepo.saveAll(tickers);
		return weeklyOptionCnt;
	}
}
