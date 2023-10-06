package org.rhm.stock.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.rhm.stock.domain.IbdStatistic;
import org.rhm.stock.domain.SignalType;
import org.rhm.stock.domain.SignalTypeCount;
import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.dto.StockSignalDisplay;
import org.rhm.stock.repository.AveragePriceRepo;
import org.rhm.stock.repository.IbdStatisticRepo;
import org.rhm.stock.repository.SignalRepo;
import org.rhm.stock.repository.SignalTypeCountRepo;
import org.rhm.stock.repository.SignalTypeRepo;
import org.rhm.stock.util.StockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class SignalService {
	@Autowired
	private SignalTypeRepo signalTypeRepo = null;
	@Autowired
	private SignalRepo signalRepo = null;
	@Autowired
	private SignalTypeCountRepo sigCntRepo = null;
	@Autowired
	private IbdStatisticRepo ibdRepo = null;
	@Autowired
	private AveragePriceRepo avgPriceRepo = null;

	private Logger logger = LoggerFactory.getLogger(SignalService.class);
	
	public SignalType createSignalType(SignalType signalType) {
		SignalType sigType = signalTypeRepo.save(signalType);
		return sigType;
	}
	
	public List<SignalType> signalTypes() {
		return signalTypeRepo.findAll().stream()
				.sorted((o1,o2)->o1.getSignalDesc()
					.compareTo(o2.getSignalDesc()))
				.collect(Collectors.toList());
	}
	
	public StockSignal createSignal(StockSignal signal) {
		StockSignal newSignal = null;
		Example<StockSignal> example = Example.of(signal);
		if (!signalRepo.exists(example)) {
			newSignal = signalRepo.save(signal);
		}
		else {
			logger.debug("createSignal - signal " + signal.getSignalId() + " already exists");
		}
		return newSignal;
	}
	
	public List<StockSignal> findSignals(String signalType) {
		return signalRepo.findBySignalTypeOrderByPriceId(signalType);
	}
	
	public List<StockSignal> findSignalsUnsorted(String signalType) {
		return signalRepo.findBySignalType(signalType);
	}
	
	public List<StockSignal> findSignalsByType(List<String> signalTypes) {
		return this.findSignalsByType(signalTypes, 7);
	}
	
	public List<StockSignal> findSignalsByPriceId(String priceId) {
		return signalRepo.findByPriceId(priceId);
	}
	
	public List<StockSignal> findSignalsByType(List<String> signalTypes, int lookBackDays) {
		logger.debug("findSignalsByType - signalTypes: " + signalTypes.toString() + "; lookBackDays: " + lookBackDays);
		StockSignal latestSignal = this.findMaxDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(latestSignal.getPriceDate());
		cal.add(Calendar.DAY_OF_MONTH, lookBackDays * -1);
		return signalRepo.findSignalsByType(signalTypes, cal.getTime());
	}
	
	public StockSignal findMaxDate() {
		return signalRepo.findTopByOrderByPriceDateDesc();
	}
	
	public List<StockSignalDisplay> findSignalsByTypeAndDate(String signalType, String priceDateStr) {
		List<StockSignal> signalList = null;
		Date priceDate = null;
		try {
			priceDate = StockUtil.stringToDate(priceDateStr);
		} 
		catch (ParseException e) {
			logger.warn("findSignalsByTypeAndDate - " + e.getMessage());
		}
		signalList = this.findSignalsByTypeAndDate(signalType, priceDate);
		return this.transformSignalList(signalList, priceDate);
	}
	
	public List<StockSignalDisplay> transformSignalList(List<StockSignal> signalList, Date priceDate) {
		Map<String, IbdStatistic> ibdStatMap = this.latestIbdStats();
		Map<String, StockAveragePrice> avgPriceMap = this.avgPricesByDate(priceDate);
		List<StockSignalDisplay> sigDisplayList = new ArrayList<StockSignalDisplay>();
		if (avgPriceMap != null) {
			signalList.forEach((signal)->{
				StockSignalDisplay sigDisp = new StockSignalDisplay(signal);
				sigDisp.setAvgPrice(avgPriceMap.get(signal.getTickerSymbol()));
				sigDisp.setIbdLatestStat(ibdStatMap.get(signal.getTickerSymbol()));
				sigDisplayList.add(sigDisp);
			});
		}
		return sigDisplayList.stream().sorted((o1,o2)->{
			return o1.getTickerSymbol().compareTo(o2.getTickerSymbol());}).collect(Collectors.toList());
	}
	
	public List<StockSignalDisplay> summarizeSignals(List<StockSignal> signalList, Date priceDate, int criteriaCnt) {
		Map<String,List<StockSignal>> signalMap = new HashMap<String,List<StockSignal>>();
		List<StockSignal> matchingSignals = new ArrayList<StockSignal>();
		List<StockSignal> tickerSignals = null;
		for (StockSignal signal: signalList) {
			tickerSignals = signalMap.get(signal.getPriceId());
			if (tickerSignals == null) {
				tickerSignals = new ArrayList<StockSignal>();
				signalMap.put(signal.getPriceId(), tickerSignals);
			}
			tickerSignals.add(signal);
		}
		for (String priceId: signalMap.keySet()) {
			tickerSignals = signalMap.get(priceId);
			if (tickerSignals.size() == criteriaCnt) {
				matchingSignals.add(tickerSignals.get(0));
			}
		}
		return this.transformSignalList(matchingSignals, priceDate);
	}
	
	private List<String> extractTickerFromSignal(List<StockSignal> signalList) {
		List<String> tickerList = new ArrayList<String>();
		for (StockSignal signal: signalList) {
			tickerList.add(signal.getTickerSymbol());
		}
		logger.debug("extractTickerFromSignal - " + tickerList.toString());
		return tickerList;
	}
	
	private Map<String, IbdStatistic> latestIbdStats() {
		Map<String, IbdStatistic> ibdStatMap = new HashMap<String, IbdStatistic>();
//		IbdStatistic latestStat = ibdRepo.findTopByOrderByPriceDateDesc();
		List<IbdStatistic> latestStats = null;
		latestStats = ibdRepo.findByOrderByPriceDateDesc();
		latestStats.forEach((stat)->{
			if (ibdStatMap.get(stat.getTickerSymbol()) == null) {
				ibdStatMap.put(stat.getTickerSymbol(), stat);
			}
		});
		return ibdStatMap;
	}
	
	private Map<String, StockAveragePrice> avgPricesByDate(Date priceDate) {
		Map<String, StockAveragePrice> avgPriceMap = new HashMap<String, StockAveragePrice>();
		List<StockAveragePrice> avgPrices = avgPriceRepo.findByPriceDate(priceDate);
		if (avgPrices != null) {
			avgPrices.forEach((avgPrice)->{avgPriceMap.put(avgPrice.getTickerSymbol(), avgPrice);});
		}
		return avgPriceMap;
	}
	
	public List<StockSignalDisplay> findSignalsByTypeAndDate(String signalType, String overlaySignalType, String priceDateParam) {
		logger.debug("findSignalsByTypeAndDate - signalType=" + signalType + "; overlaySignalType=" + overlaySignalType + "; priceDate=" + priceDateParam);
		List<StockSignal> baseSignalList = null;
		Date priceDate = null;
		try {
			priceDate = StockUtil.stringToDate(priceDateParam);
		} 
		catch (ParseException e) {
			logger.warn("findSignalsByTypeAndDate - parse price date: " + e.getMessage());
		}
		Map<String, IbdStatistic> ibdStatMap = this.latestIbdStats();
		Map<String, StockAveragePrice> avgPriceMap = this.avgPricesByDate(priceDate);
		baseSignalList = this.findSignalsByTypeAndDate(signalType, priceDate);
		List<String> overlayTickerList = null;
		overlayTickerList =	this.extractTickerFromSignal(this.findSignalsByTypeAndDate(overlaySignalType, priceDate));
		logger.debug("findSignalsByTypeAndDate - " + overlayTickerList.size() + " tickers found for " + overlaySignalType + " signals");
		List<StockSignalDisplay> mergedSignalList = new ArrayList<StockSignalDisplay>();
		StockSignalDisplay signalDisplay = null;
		for (StockSignal signal: baseSignalList) {
			signalDisplay = new StockSignalDisplay(signal);
			if (overlayTickerList.contains(signalDisplay.getTickerSymbol())) {
				signalDisplay.setMultiList(true);
				logger.debug("findSignalsByTypeAndDate - multiList set to true");
			}
			else {
				signalDisplay.setMultiList(false);
			}
			signalDisplay.setAvgPrice(avgPriceMap.get(signal.getTickerSymbol()));
			signalDisplay.setIbdLatestStat(ibdStatMap.get(signal.getTickerSymbol()));
			mergedSignalList.add(signalDisplay);
		}
		return mergedSignalList;
	}
	
	public List<StockSignal> findSignalsByTypeAndDate(String signalType, Date priceDate) {
		logger.debug("findSignalsByTypeAndDate - signalType:" + signalType + ";priceDate:" + priceDate);
		return signalRepo.findBySignalTypeAndPriceDateOrderByTickerSymbol(signalType, priceDate);
	}
	
	public List<StockSignal> findSignalsByTickerAndDate(String tickerSymbol, Date priceDate) {
		return signalRepo.findByTickerSymbolAndPriceDateOrderBySignalType(tickerSymbol, priceDate);
	}
	
	public long deleteOlderThan(Date deleteBefore) {
//		return signalRepo.deleteOlderThan(deleteBefore);
		return signalRepo.deleteByPriceDateBefore(deleteBefore);
	}
	
	public List<StockSignal> findSignalsByTicker(String tickerSymbol) {
		return signalRepo.findByTickerSymbolOrderByPriceDateDesc(tickerSymbol);
	}
	
	public void saveSignalCounts(List<SignalTypeCount> sigCntList) {
//		sigCntRepo.saveAll(sigCntList);
		for (SignalTypeCount cnt: sigCntList) {
			sigCntRepo.save(cnt);
		}
	}
	
	public List<SignalTypeCount> findSignalCounts() {
		return sigCntRepo.findAll();
	}
	
	public List<SignalTypeCount> findSignalCounts(String signalCode) {
		return sigCntRepo.findBySignalCodeOrderBySignalDateDesc(signalCode);
	}

	private Date calcDate(Date priceDate, Integer offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(priceDate);
		cal.add(Calendar.DAY_OF_MONTH, offset);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			cal.add(Calendar.DAY_OF_MONTH, offset>0?2:-2);
		}
		else {
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				cal.add(Calendar.DAY_OF_MONTH, offset>0?1:-1);
			}
		}
		return cal.getTime();
	}
	
	public List<StockSignal> findSignalsByTicker(String tickerSymbol, Date priceDate) {
		List<StockSignal> signals = signalRepo.findByTickerSymbolOrderByPriceDateDesc(tickerSymbol);
		Date beginDate = this.calcDate(priceDate, -3), endDate = this.calcDate(priceDate, 3);
		return signals.stream().filter(
			(signal)->{return (signal.getPriceDate().compareTo(beginDate) > 0 
				&& signal.getPriceDate().compareTo(endDate) < 0);}).collect(Collectors.toList());
	}
	
	public long deleteByTickerSymbol(String tickerSymbol) {
		return signalRepo.deleteByTickerSymbol(tickerSymbol);
	}
	
	public List<String> findSignalTickers(Date priceDate) {
		List<String> tickerSymbolList = signalRepo.findUniqueTickerSymbols(priceDate);
		return tickerSymbolList;
	}
}
