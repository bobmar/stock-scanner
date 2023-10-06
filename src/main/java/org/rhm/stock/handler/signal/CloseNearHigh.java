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
@Qualifier("closeNearHigh")
public class CloseNearHigh implements SignalScanner {
	@Autowired
	private PriceService priceSvc = null;
	@Autowired
	private SignalService signalSvc = null;
	private static final String CLOSE_NEAR_HIGH_SIGNAL = "CLSHI";
	private Logger logger = LoggerFactory.getLogger(CloseNearHigh.class);
	private void evaluatePrice(StockPrice price) {
		double highCloseRange = price.getHighPrice().doubleValue() - price.getClosePrice().doubleValue();
		if (highCloseRange / price.getHighLowRange().doubleValue() < .1) {
			signalSvc.createSignal(new StockSignal(price, CLOSE_NEAR_HIGH_SIGNAL));
			logger.debug("evaluatePrice - create close near high stat: high=" + price.getHighPrice() + "; low=" + price.getLowPrice() + "; close=" + price.getClosePrice());
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockPrice> priceList = priceSvc.retrievePrices(tickerSymbol);
		logger.info("scan - found " + priceList.size() + " prices for " + tickerSymbol);
		for (StockPrice price: priceList) {
			this.evaluatePrice(price);
		}
	}

}
