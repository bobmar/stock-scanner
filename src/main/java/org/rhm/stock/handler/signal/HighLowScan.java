package org.rhm.stock.handler.signal;

import java.util.List;

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
@Qualifier("highLowScan")
public class HighLowScan implements SignalScanner {

	private final static String HHHL_SIGNAL = "HHHL3";
	private final static String LLLH_SIGNAL = "LLLH3";
	private final static String HHHL_UP_SIGNAL = "HHHL3U";
	private final static String LLLH_DN_SIGNAL = "LLLH3D";

	@Autowired
	private PriceService priceSvc = null;
	@Autowired
	private SignalService signalSvc = null;
	@Autowired
	private StatisticService statSvc = null;
	private Logger logger = LoggerFactory.getLogger(HighLowScan.class);
	private void detectFollowThrough(StockPrice price, List<StockStatistic> statList, String signalType) {
		int statIdx = -1;
		for (int i = 0; i < statList.size(); i++) {
			if (statList.get(i).getPriceDate().compareTo(price.getPriceDate()) == 1) {
				statIdx = i;
			}
			else {
				break;
			}
		}
		switch (signalType) {
		case HHHL_SIGNAL:
			if (statIdx > 0 && 
					(statList.get(statIdx).getStatisticValue().doubleValue() > 0 ||
					statList.get(statIdx -1).getStatisticValue().doubleValue() > 0)
				) {
				signalSvc.createSignal(new StockSignal(price, HHHL_UP_SIGNAL));
			}
			break;
		case LLLH_SIGNAL:
			if (statIdx > 0 && 
					(statList.get(statIdx).getStatisticValue().doubleValue() < 0 ||
					statList.get(statIdx -1).getStatisticValue().doubleValue() < 0)
				) {
				signalSvc.createSignal(new StockSignal(price, LLLH_DN_SIGNAL));				
			}
			break;
		}
	}
	
	private void detectHigherHighHigherLow(List<StockPrice> priceList, List<StockStatistic> statList) {
		StockPrice currPrice = null, prevPrice = null;
		int days = 0;
		for (int i = 0; i < priceList.size() - 1; i++) {
			currPrice = priceList.get(i);
			prevPrice = priceList.get(i + 1);
			if (currPrice.getHighPrice().compareTo(prevPrice.getHighPrice()) == 1
					&& currPrice.getLowPrice().compareTo(prevPrice.getLowPrice()) == 1) {
				days++;
			}
			else {
				break;
			}
		}
		if (days == 3) {
			logger.info("detectHigherHighHigherLow - " + currPrice.getPriceId() + " found higher high, higher low");
			signalSvc.createSignal(new StockSignal(priceList.get(0), HHHL_SIGNAL));
			this.detectFollowThrough(priceList.get(0), statList, HHHL_SIGNAL);
		}
	}
	
	private void detectLowerLowLowerHigh(List<StockPrice> priceList, List<StockStatistic> statList) {
		StockPrice currPrice = null, prevPrice = null;
		int days = 0;
		for (int i = 0; i < priceList.size() - 1; i++) {
			currPrice = priceList.get(i);
			prevPrice = priceList.get(i + 1);
			if (currPrice.getHighPrice().compareTo(prevPrice.getHighPrice()) == -1
					&& currPrice.getLowPrice().compareTo(prevPrice.getLowPrice()) == -1) {
				days++;
			}
			else {
				break;
			}
		}
		if (days == 3) {
			logger.info("detectLowerLowLowerHigh - " + currPrice.getPriceId() + " found lower low, lower high" );
			signalSvc.createSignal(new StockSignal(priceList.get(0), LLLH_SIGNAL));
			this.detectFollowThrough(priceList.get(0), statList, LLLH_SIGNAL);
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockPrice> priceList = priceSvc.retrievePrices(tickerSymbol);
		List<StockStatistic> statList = statSvc.retrieveStatList(tickerSymbol, "DYPCTCHG");
		logger.info("scan - found " + priceList.size() + " prices for " + tickerSymbol);
		logger.info("scan - found " + statList.size() + " DYPCTCHG stats for " + tickerSymbol);
		while (priceList.size() > 4) {
			this.detectHigherHighHigherLow(priceList.subList(0, 4), statList);
			this.detectLowerLowLowerHigh(priceList.subList(0, 4), statList);
			priceList.remove(0);
		}
	}
}
