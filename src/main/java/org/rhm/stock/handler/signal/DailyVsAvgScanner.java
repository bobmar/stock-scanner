package org.rhm.stock.handler.signal;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.handler.stat.DailyPriceVsAvg;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.SignalService;
import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("dailyVsAvgCrossover")
public class DailyVsAvgScanner implements SignalScanner {
	@Autowired
	private StatisticService statSvc;
	@Autowired
	private SignalService signalSvc;
	@Autowired
	private PriceService priceSvc;
	private static final String SIGNAL_DLYV50X_UP = "DYV50XUP";
	private static final String SIGNAL_DLYV50X_DN = "DYV50XDN";
	private static final String SIGNAL_DLYV200X_UP = "DYV200XUP";
	private static final String SIGNAL_DLYV200X_DN = "DYV200XDN";
	private static final String SIGNAL_20ABV200 = "AVG20ABV200";
	private static final String SIGNAL_DLYV50_0507 = "DLYV500507";
	private static final String SIGNAL_DLYABV20 = "DLYABV20A";
	private static final String STAT_DYV20 = "DYPRCV20A";
	private static final String STAT_DYV50 = "DYPRCV50A";
	private static final String STAT_DYV200 = "DYPRCV200A";
	private static final Logger LOGGER = LoggerFactory.getLogger(DailyVsAvgScanner.class);
	private final Map<String, StockPrice> priceMap = new HashMap<String, StockPrice>();
	
	private StockPrice findStockPrice(String priceId) {
		StockPrice price = priceMap.get(priceId);
		if (price == null) {
			price = priceSvc.findStockPrice(priceId);
			if (price != null) {
				priceMap.put(price.getPriceId(), price);
			}
			else {
				LOGGER.warn("findStockPrice - priceId: {} was not found", priceId);
			}
		}
		return price;
	}
	
	private void evaluateCrossUp(List<StockStatistic> statList, String signalType) {
		StockPrice price = null;
		StockSignal signal = null;
		if (statList.size() == 4) {
			if (statList.get(0).getStatisticValue() > 1 && statList.get(1).getStatisticValue() > 1) {
				if (statList.get(2).getStatisticValue() < 1 && statList.get(3).getStatisticValue() < 1) {
					price = this.findStockPrice(statList.get(1).getPriceId());
					signal = new StockSignal(price, signalType);
					signalSvc.createSignal(signal);
					LOGGER.debug("evaluateCrossUp - created {} signal for {}", signalType, signal.getPriceId());
				}
			}
		}
	}
	
	private void evaluateCrossDown(List<StockStatistic> statList, String signalType) {
		StockPrice price = null;
		StockSignal signal = null;
		if (statList.size() == 4) {
			if (statList.get(0).getStatisticValue() < 1 && statList.get(1).getStatisticValue() < 1) {
				if (statList.get(2).getStatisticValue() > 1 && statList.get(3).getStatisticValue() > 1) {
					price = this.findStockPrice(statList.get(1).getPriceId());
					signal = new StockSignal(price, signalType);
					signalSvc.createSignal(signal);
					LOGGER.debug("evaluateCrossDown - created {} signal for {}", signalType, signal.getPriceId());
				}
			}
		}
	}

	private void evaluate20Above200(StockStatistic stat) {
		StockPrice price = null;
		StockSignal signal = null;
		if (stat.getStatisticValue() > 1) {
			price = this.findStockPrice(stat.getPriceId());
			signal = new StockSignal(price, SIGNAL_20ABV200);
			signalSvc.createSignal(signal);
			LOGGER.debug("evaluate20Above200 - created {} signal for {}", SIGNAL_20ABV200, signal.getPriceId());
		}
	}
	
	private void evaluateCloseAbove20(StockStatistic stat) {
		StockPrice price = null;
		StockSignal signal = null;
		if (stat.getStatisticValue() > 1) {
			price = this.findStockPrice(stat.getPriceId());
			signal = new StockSignal(price, SIGNAL_DLYABV20);
			signalSvc.createSignal(signal);
			LOGGER.debug("evaluateCloseAbove20 - created {} signal for {}", SIGNAL_DLYABV20, signal.getPriceId());
		}
	}
	
	private void evaluateCloseVs50(StockStatistic stat) {
		StockPrice price = null;
		if (stat.getStatisticValue() > 1.04 && stat.getStatisticValue() < 1.08) {
			price = this.findStockPrice(stat.getPriceId());
			signalSvc.createSignal(new StockSignal(price, SIGNAL_DLYV50_0507));
			LOGGER.debug("evaluateCloseVs50 - created {} signal for {}", SIGNAL_DLYV50_0507, stat.getPriceId());
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockStatistic> statList = statSvc.retrieveStatList(tickerSymbol, STAT_DYV50);
		LOGGER.info("scan - found {} {} stats for {}", statList.size(), STAT_DYV50, tickerSymbol);
		while (statList.size() > 4) {
			this.evaluateCrossUp(statList.subList(0, 4), SIGNAL_DLYV50X_UP);
			this.evaluateCrossDown(statList.subList(0, 4), SIGNAL_DLYV50X_DN);
			this.evaluateCloseVs50(statList.get(0));
			statList.remove(0);
		}
		statList = statSvc.retrieveStatList(tickerSymbol, STAT_DYV20);
		LOGGER.info("scan - found {} {} stats for {}", statList.size(), STAT_DYV50, tickerSymbol);
		while (statList.size() > 4) {
			this.evaluateCloseAbove20(statList.get(0));
			statList.remove(0);
		}
		statList = statSvc.retrieveStatList(tickerSymbol, STAT_DYV200);
		LOGGER.info("scan - found {} {} stats for {}", statList.size(), STAT_DYV200, tickerSymbol);
		while (statList.size() > 4) {
			this.evaluateCrossUp(statList.subList(0, 4), SIGNAL_DLYV200X_UP);
			this.evaluateCrossDown(statList.subList(0, 4), SIGNAL_DLYV200X_DN);
			statList.remove(0);
		}
		statList = statSvc.retrieveStatList(tickerSymbol, DailyPriceVsAvg.AVG_PRC_20_VS_200);
		for (StockStatistic stat: statList) {
			this.evaluate20Above200(stat);
		}
	}

}
