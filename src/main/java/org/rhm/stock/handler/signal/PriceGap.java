package org.rhm.stock.handler.signal;

import java.util.List;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.SignalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
@Component
@Qualifier("priceGap")
public class PriceGap implements SignalScanner {
	@Autowired
	private PriceService priceSvc = null;
	@Autowired
	private SignalService signalSvc = null;
	
	private static final String GAP_UP_SIGNAL = "GAPUP";
	private static final String GAP_DN_SIGNAL = "GAPDN";
	private static final String INSIDE_DAY_SIGNAL = "INSIDE";
	private Logger logger = LoggerFactory.getLogger(PriceGap.class);
	
	private boolean gapUp(StockPrice currPrice, StockPrice prevPrice) {
		boolean gapUp = false;
		if (currPrice.getLowPrice().compareTo(prevPrice.getHighPrice()) == 1) {
			gapUp = true;
			logger.debug("gapUp - found gap up for " + currPrice.getPriceId());
		}
		return gapUp;
	}
	
	private boolean gapDown(StockPrice currPrice, StockPrice prevPrice) {
		boolean gapDown = false;
		if (currPrice.getHighPrice().compareTo(prevPrice.getLowPrice()) == -1) {
			gapDown = true;
			logger.debug("gapDown - found gap down for " + currPrice.getPriceId());
		}
		return gapDown;
	}
	
	private void detectPriceGap(List<StockPrice> priceList) {
		if (priceList.size() < 2) {
			logger.warn("detectPriceGap -  need 2 prices for evaluation");
		}
		else {
			StockPrice currPrice = priceList.get(0), prevPrice = priceList.get(1);
			logger.debug("detectPriceGap - check " + currPrice.getPriceId() 
				+ " current open=" + currPrice.getOpenPrice() 
				+ "; previous high=" + prevPrice.getHighPrice() 
				+ "; previous low=" + prevPrice.getLowPrice());
			if (this.gapUp(currPrice, prevPrice)) {
				signalSvc.createSignal(new StockSignal(currPrice, GAP_UP_SIGNAL));
			}
			else {
				if (this.gapDown(currPrice, prevPrice)) {
					signalSvc.createSignal(new StockSignal(currPrice, GAP_DN_SIGNAL));
				}
			}
		}
	}
	
	private void detectInsideDay(List<StockPrice> priceList) {
		if (priceList.size() < 2) {
			logger.warn("detectInsideDay - need 2 prices for evaluation");
		}
		else {
			StockPrice currPrice = priceList.get(0), prevPrice = priceList.get(1);
			if (currPrice.getHighPrice().doubleValue() < prevPrice.getHighPrice().doubleValue()) {
				if (currPrice.getLowPrice().doubleValue() > prevPrice.getLowPrice().doubleValue()) {
					signalSvc.createSignal(new StockSignal(currPrice, INSIDE_DAY_SIGNAL));
				}
			}
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockPrice> priceList = priceSvc.retrievePrices(tickerSymbol);
		logger.info("scan - found " + priceList.size() + " prices for " + tickerSymbol);
		while (priceList.size() >= 2) {
			this.detectPriceGap(priceList.subList(0, 2));
			this.detectInsideDay(priceList.subList(0, 2));
			priceList.remove(0);
			logger.debug("scan - " + priceList.size() + " prices remaining");
		}
	}
}
