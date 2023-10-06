package org.rhm.stock.handler.signal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.SignalService;
import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("stdDeviation")
public class StdDeviationScanner implements SignalScanner {
	@Autowired
	private StatisticService statSvc = null;
	@Autowired
	private PriceService priceSvc = null;
	@Autowired
	private SignalService sigSvc = null;
	private static final String STAT_2WK_SD = "STDDEV2WK";
	private static final String STAT_10WK_SD = "STDDEV10WK";
	private static final String SIGNAL_2GT10_SD = "SD2GT10";
	private Logger logger = LoggerFactory.getLogger(StdDeviationScanner.class);
	private void listToMap(Map<String,StockStatistic> statMap, List<StockStatistic> statList) {
		for (StockStatistic stat: statList) {
			statMap.put(stat.getPriceId(), stat);
		}
	}

	private void processStats(List<StockStatistic> stdDevList, Map<String,StockStatistic> statMap) {
		StockPrice price = null;
		StockStatistic statSd10Wk = null;
		StockSignal signal = null;
		for (StockStatistic stat: stdDevList) {
			statSd10Wk = statMap.get(stat.getPriceId());
			if (stat.getStatisticValue().compareTo(statSd10Wk.getStatisticValue()) == 1) {
				price = priceSvc.findStockPrice(stat.getPriceId());
				signal = new StockSignal(price, SIGNAL_2GT10_SD);
				sigSvc.createSignal(signal);
			}
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockStatistic> stdDev2WkList = statSvc.retrieveStatList(tickerSymbol, STAT_2WK_SD);
		logger.info("scan - found " + stdDev2WkList.size() + " " + STAT_2WK_SD + " stats for " + tickerSymbol);
		List<StockStatistic> stdDev10WkList = statSvc.retrieveStatList(tickerSymbol, STAT_10WK_SD);
		logger.info("scan - found " + stdDev10WkList.size() + " " + STAT_10WK_SD + " stats for " + tickerSymbol);
		Map<String,StockStatistic> statMap = new HashMap<String,StockStatistic>();
		this.listToMap(statMap, stdDev10WkList);
		this.processStats(stdDev2WkList, statMap);
	}

}
