package org.rhm.stock.service;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.dto.CompositePrice;
import org.rhm.stock.repository.IbdStatisticRepo;
import org.rhm.stock.repository.PriceRepo;
import org.rhm.stock.repository.StatisticRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CompositePriceService {
	@Autowired
	private StatisticRepo statRepo;
	@Autowired
	private PriceRepo priceRepo;
	@Autowired
	private SignalService signalSvc;
	@Autowired
	private AveragePriceService avgPriceSvc;
	@Autowired
	private IbdStatisticRepo ibdRepo;
	
	private Map<String, List<StockSignal>> signalListToMap(List<StockSignal> signalList) {
		Map<String, List<StockSignal>> signalMap = new HashMap<String, List<StockSignal>>();
		List<StockSignal> currSignalList = null;
		for (StockSignal signal: signalList) {
			currSignalList = signalMap.get(signal.getPriceId());
			if (currSignalList == null) {
				currSignalList = new ArrayList<StockSignal>();
				signalMap.put(signal.getPriceId(), currSignalList);
			}
			currSignalList.add(signal);
		}
		return signalMap;
	}
	
	public List<CompositePrice> findSignals(List<String> signalTypeList, Integer lookBackDays) {
		List<StockSignal> signalList = signalSvc.findSignalsByType(signalTypeList, lookBackDays);
		return this.transformSignalList(signalList);
	}
	
	public List<CompositePrice> transformSignalList(List<StockSignal> signalList) {
		List<CompositePrice> compPriceList = new ArrayList<CompositePrice>();
		Map<String, List<StockSignal>> signalMap = this.signalListToMap(signalList);
		CompositePrice cPrice = null;
		String tickerSymbol = null;
		for (String priceId: signalMap.keySet()) {
			cPrice = new CompositePrice();
			cPrice.setPriceId(priceId);
			cPrice.setPrice(priceRepo.findById(priceId).get());
			cPrice.setSignalList(signalMap.get(priceId));
			cPrice.setStatisticList(statRepo.findByPriceId(priceId));
			tickerSymbol = cPrice.getStatisticList().get(0).getTickerSymbol();
			cPrice.setTickerSymbol(tickerSymbol);
			compPriceList.add(cPrice);
		}
		return compPriceList;
	}

	public List<CompositePrice> compositePriceFactory(List<String> priceIdList) {
		List<CompositePrice> compPriceList = new ArrayList<CompositePrice>();
		CompositePrice cPrice = null;
		for (String priceId: priceIdList) {
			cPrice = this.compositePriceFactory(priceId);
			compPriceList.add(cPrice);
		}
		return compPriceList;
	}
	
	public CompositePrice compositePriceFactory(String priceId) {
		CompositePrice cPrice = null;
		cPrice = new CompositePrice();
		cPrice.setPriceId(priceId);
		cPrice.setPrice(priceRepo.findById(priceId).get());
		cPrice.setSignalList(signalSvc.findSignalsByPriceId(priceId));
		cPrice.setStatisticList(statRepo.findByPriceId(priceId));
		cPrice.setTickerSymbol(cPrice.getPrice().getTickerSymbol());
		cPrice.setIbdStatList(ibdRepo.findByTickerSymbol(cPrice.getPrice().getTickerSymbol()));
		cPrice.setAvgPrices(avgPriceSvc.findRecentAvgPriceList(cPrice.getTickerSymbol()));
		cPrice.setHistSignals(this.historicalSignalMap(cPrice.getPrice()));
		return cPrice;
	}
	
	private Map<String,List<StockSignal>> historicalSignalMap(StockPrice price) {
		Map<String,List<StockSignal>> histSignalMap = new HashMap<String,List<StockSignal>>();
		List<StockPrice> histPriceList = priceRepo.findTop60ByTickerSymbolOrderByPriceDateDesc(price.getTickerSymbol());
		StockPrice workingPrice = null;
		List<StockSignal> workingSignals = null;
		Integer[] lookbackDays = {20,40,60};
		String mapKey = null;
		for (Integer lookbackDay: lookbackDays) {
			if (histPriceList.size() >= lookbackDay) {
				workingPrice = histPriceList.get(lookbackDay - 1);
				workingSignals = signalSvc.findSignalsByPriceId(workingPrice.getPriceId());
				switch (lookbackDay) {
				case 20:
					mapKey = "fourWeek";
					break;
				case 40:
					mapKey = "eightWeek";
					break;
				case 60:
					mapKey = "twelveWeek";
					break;
				}
				histSignalMap.put(mapKey, workingSignals);
			}
		}
		return histSignalMap;
	}
	
}
