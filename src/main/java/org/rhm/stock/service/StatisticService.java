package org.rhm.stock.service;

import org.rhm.stock.domain.StatisticType;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.repository.StatisticRepo;
import org.rhm.stock.repository.StatisticTypeRepo;
import org.rhm.stock.util.StockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class StatisticService {
	@Autowired
	private StatisticRepo statRepo;
	@Autowired
	private StatisticTypeRepo statTypeRepo;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(StatisticService.class);
	
	public StockStatistic createStatistic(StockStatistic stat) {
		return this.createStatistic(stat, true);
	}
	
	public StockStatistic createStatistic(StockStatistic stat, boolean forceUpdate) {
		StockStatistic newStat = null;
		if (!statRepo.exists(Example.of(stat))) {
			newStat = statRepo.save(stat);
			LOGGER.debug("createStatistic - " + stat.getStatId() + " doesn't exist; created new");
		}
		else {
			if (forceUpdate) {
				newStat = statRepo.save(stat);
				LOGGER.debug("createStatistic - " + stat.getStatId() + " already exists; updated");
			}
			else {
				LOGGER.debug("createStatistic - " + stat.getStatId() + " already exists and force update not specified; skipping");
			}
		}
		return newStat;
	}
	
	public List<StockStatistic> retrieveStatList(String tickerSymbol) {
		return statRepo.findByTickerSymbol(tickerSymbol)
				.stream()
				.sorted((o1,o2)->{
						int compare = o1.getPriceId().compareTo(o2.getPriceId()) * -1;
						if (compare == 0) {
							compare = o1.getStatisticType().compareTo(o2.getStatisticType());
						}
						return compare;
					})
				.collect(Collectors.toList());
	}
	
	public List<StockStatistic> retrieveStatList(String tickerSymbol, String statisticType) {
		return statRepo.findByTickerSymbolAndStatisticType(tickerSymbol, statisticType)
				.stream()
				.sorted((o1,o2)->{return o1.getStatId().compareTo(o2.getStatId()) * -1;})
				.collect(Collectors.toList());
	}
	
	public StockStatistic retrieveStat(String tickerSymbol, String statisticType, Date priceDate) {
		List<StockStatistic> statList = statRepo.findByTickerSymbolAndStatisticTypeAndPriceDate(tickerSymbol, statisticType, priceDate);
		StockStatistic stat = null;
		if (statList.size() > 0) {
			stat = statList.get(0);
		}
		return stat;
	}
	
	public StockStatistic retrieveStat(String statId) {
		Optional<StockStatistic> optStat = statRepo.findById(statId);
		StockStatistic stat = optStat.isPresent()?optStat.get():null;
		return stat;
	}
	
	public Map<String, List<StockStatistic>> retrieveStatMap(String tickerSymbol) {
		Map<String, List<StockStatistic>> statMap = new HashMap<String, List<StockStatistic>>();
		List<StockStatistic> fullStatList = this.retrieveStatList(tickerSymbol), currStatList = null;
		for (StockStatistic stat: fullStatList) {
			currStatList = statMap.get(stat.getPriceId());
			if (currStatList == null) {
				currStatList = new ArrayList<StockStatistic>();
				statMap.put(stat.getPriceId(), currStatList);
			}
			currStatList.add(stat);
		}
		return statMap;
	}
	
	public Map<String,List<StockStatistic>> retrieveStatMap(Date priceDate, Integer days, String statType) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(priceDate);
		cal.add(Calendar.DAY_OF_MONTH, days * -1);
		Date toDate = priceDate, fromDate = cal.getTime();
		return this.retrieveStatMap(fromDate, toDate, statType);
	}
	
	public Map<String,List<StockStatistic>> retrieveStatMap(Date fromDate, Date toDate, String statType) {
		List<StockStatistic> statList = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,List<StockStatistic>> statMap = new HashMap<String,List<StockStatistic>>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.HOUR_OF_DAY, 7);
		while (cal.getTime().compareTo(toDate) <= 0) {
			if (cal.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK) < Calendar.SATURDAY ) {
				statList = statRepo.findByStatisticTypeAndPriceDate(statType, cal.getTime())
						.stream()
						.sorted((o1,o2)->{return o1.getStatisticValue().compareTo(o2.getStatisticValue());})
						.collect(Collectors.toList());
				statMap.put(df.format(cal.getTime()), statList);
				LOGGER.debug("retrieveStatMap - added " + cal.getTime() + " to map");
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return statMap;
	}
	
	public List<StockStatistic> retrieveStatList(Date statDate, String statType) {
		Date mongoDate = null;
		try {
			mongoDate = StockUtil.toMongoDate(statDate);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		return statRepo.findByStatisticTypeAndPriceDate(statType, mongoDate)
				.stream()
				.sorted((o1,o2)->{return o1.getStatisticValue().compareTo(o2.getStatisticValue());})
				.collect(Collectors.toList());
	}
	
	public StockStatistic findMaxDate() {
		return statRepo.findTopByOrderByPriceDateDesc();
	}
	
	public StatisticType createStatType(StatisticType statType) {
		return statTypeRepo.save(statType);
	}
	
	public List<StatisticType> retrieveStatTypeList() {
		return statTypeRepo.findAll()
				.stream()
				.sorted((o1,o2)->{return o1.getStatisticDesc().compareTo(o2.getStatisticDesc());})
				.collect(Collectors.toList());
	}
	
	public List<StatisticType> retrieveDashboardStatTypeList() {
		return statTypeRepo.findByShowInDashboard(true)
				.stream()
				.sorted((o1,o2)->{return o1.getStatisticDesc().compareTo(o2.getStatisticDesc());})
				.collect(Collectors.toList());
	}
	
	public long deleteOlderThan(Date deleteBefore) {
//		return statRepo.deleteOlderThan(deleteBefore);
		return statRepo.deleteByPriceDateBefore(deleteBefore);
	}
	
	public long deleteByTickerSymbol(String tickerSymbol) {
		return statRepo.deleteByTickerSymbol(tickerSymbol);
	}

	public List<String> findStatTickers(Date priceDate) {
		List<String> tickerSymbols = new ArrayList<String>();
		tickerSymbols = statRepo.findUniqueTickerSymbols(priceDate);
		return tickerSymbols;
	}
}
