package org.rhm.stock.service;

import org.rhm.stock.controller.dto.TickerInfo;
import org.rhm.stock.domain.IbdStatistic;
import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.handler.ticker.ExcelTransformer;
import org.rhm.stock.handler.ticker.ExcelTransformerResponse;
import org.rhm.stock.io.CboeWeekly;
import org.rhm.stock.io.CompanyInfoDownload;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TickerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TickerService.class);
	@Autowired
	private TickerRepo tickerRepo;
	@Autowired
	private ExcelTransformer excel;
	@Autowired
	private IbdStatisticRepo ibdRepo;
	@Autowired
	private PriceRepo priceRepo;
	@Autowired
	private CboeWeekly cboeWeekly;
	@Autowired
	private CompanyInfoDownload companyInfoDownload;
	public String createTicker(String tickerSymbol) {
		StockTicker stockTicker = null;
		String message = null;
		Map<String,Object> companyInfo = this.companyInfoDownload.retrieveCompanyInfo(tickerSymbol);
		if (companyInfo.get("companyName") == null) {
			message = String.format("%s: %s", tickerSymbol, companyInfo.get("message"));
		}
		else {
			if (tickerRepo.existsById(tickerSymbol)) {
				message = String.format("Ticker %s already exists", tickerSymbol);
			}
			else {
				stockTicker = new StockTicker();
				stockTicker.setTickerSymbol(tickerSymbol);
				stockTicker.setCompanyName((String) companyInfo.get("companyName"));
				stockTicker.setIndustryName((String) companyInfo.get("industry"));
				stockTicker.setSectorName((String) companyInfo.get("sector"));
				tickerRepo.insert(stockTicker);
				message = String.format("%s/%s was successfully created", tickerSymbol, stockTicker.getCompanyName());
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
		String status;
		for (TickerInfo ticker: tickerList) {
			status = this.createTicker(ticker.getTickerSymbol());
			LOGGER.info("saveTickerList - {}", status);
			if (status.contains("successfully created")) {
				tickersSavedCnt++;
			}
		}
		return tickersSavedCnt;
	}
	
	public List<TickerInfo> retrieveTickerInfo(byte[] workbookBytes) {
		List<TickerInfo> tickerInfoList = new ArrayList<TickerInfo>();
		ExcelTransformerResponse response = excel.extractTickerSymbols(workbookBytes);
		List<IbdStatistic> ibdStatList = response.getIbdStatList();
		List<String> tickerList = response.getTickerSymbols();
		Map<String,Object> profile;
		TickerInfo ticker;
		for (String tickerSymbol: tickerList) {
			ticker = new TickerInfo();
			ticker.setTickerSymbol(tickerSymbol);
			if (this.tickerExists(tickerSymbol)) {
				ticker.setStatus("Ticker already exists; will not be created");
			}
			else {
				profile = this.findCompanyProfile(tickerSymbol);
				if (profile != null && profile.get("companyName") != null) {
					LOGGER.info("retrieveTickerInfo - company profile: {}", profile.toString());
					ticker.setStatus("OK");
					ticker.setCompanyName((String) profile.get("companyName"));
					ticker.setIndustry((String)profile.get("industry"));
					ticker.setSector((String)profile.get("sector"));
				}
				else {
					LOGGER.warn("retrieveTickerInfo - company profile not found for {}", tickerSymbol);
					ticker.setStatus("No company profile was found");
				}
			}
			tickerInfoList.add(ticker);
			LOGGER.info("retrieveTickerInfo - {}:{}", ticker.getTickerSymbol(), ticker.getStatus());
		}
		this.loadIbdStatistics(response.getIbdStatList(), response.getListName());
		return tickerInfoList.stream()
				.sorted((o1,o2)->{return o1.getTickerSymbol().compareTo(o2.getTickerSymbol());})
				.collect(Collectors.toList());
	}
	
	private void loadIbdStatistics(List<IbdStatistic> ibdStatList, String listName) {
		List<StockPrice> priceList = null;
		LOGGER.info(String.format("loadIbdStatistics - loading %s IBD stats", ibdStatList.size()) );
		IbdStatistic mrIbdStat = null;
		for (IbdStatistic ibdStat: ibdStatList) {
			mrIbdStat = ibdRepo.findTopByTickerSymbolOrderByPriceDateDesc(ibdStat.getTickerSymbol());
			ibdStat.getListName().add(listName);
			if (mrIbdStat != null) {
				priceList = priceRepo.findByTickerSymbolAndPriceDateGreaterThan(mrIbdStat.getTickerSymbol(), mrIbdStat.getPriceDate());
				if (priceList != null && !priceList.isEmpty()) {
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
					LOGGER.info("loadIbdStatistics - new IBD stat ticker: " + ibdStat.getTickerSymbol());
				}
			}
		}
	}

	private void createIbdStat(IbdStatistic ibdStat, String priceId, Date priceDate) {
		LOGGER.info("createIbdStat - priceId: " + priceId);
		Optional<IbdStatistic> existingStat = ibdRepo.findById(priceId);
		if (existingStat.isPresent()) {
			LOGGER.info("createIbdStat - found existing IBD stat");
			List<String> newListNames = ibdStat.getListName();
			LOGGER.info("createIbdStat - list count: " + newListNames.size());
			for (String list: existingStat.get().getListName()) {
				if (!newListNames.contains(list)) {
					newListNames.add(list);
				}
			}
			LOGGER.info("createIbdStat - list count after adding existing: " + newListNames.size());
			ibdStat.setListName(newListNames);
			if (ibdStat.getListName().size() > 1) {
				LOGGER.info("createIbdStat - " + priceId + " is on multiple IBD lists");
			}
		}
		else {
			LOGGER.info("createIbdStat - no existing IBD stat for " + priceId);
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
	
	public Map<String,Object> findCompanyProfile(String tickerSymbol) {
		return this.companyInfoDownload.retrieveCompanyInfo(tickerSymbol);
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
			if (ticker.getWeeklyOptions() != null && !ticker.getWeeklyOptions()) {
				if (weeklyTickers.contains(ticker.getTickerSymbol())) {
					ticker.setWeeklyOptions(true);
					weeklyOptionCnt++;
				}
				else {
					ticker.setWeeklyOptions(false);
				}
			}
		}
		tickerRepo.saveAll(tickers);
		return weeklyOptionCnt;
	}
}
