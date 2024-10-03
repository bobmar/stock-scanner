package org.rhm.stock.handler.signal;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.SignalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("closeNearHigh")
public class CloseNearHigh implements SignalScanner {
	@Autowired
	private PriceService priceSvc;
	@Autowired
	private SignalService signalSvc;
	private static final String CLOSE_NEAR_HIGH_SIGNAL = "CLSHI";
	private static final Logger LOGGER = LoggerFactory.getLogger(CloseNearHigh.class);
	private void evaluatePrice(StockPrice price) {
		double highCloseRange = price.getHighPrice() - price.getClosePrice();
		if (highCloseRange / price.getHighLowRange() < .1) {
			signalSvc.createSignal(new StockSignal(price, CLOSE_NEAR_HIGH_SIGNAL));
			LOGGER.debug("evaluatePrice - create close near high stat: high={}; low={}; close={}", price.getHighPrice(), price.getLowPrice(), price.getClosePrice());
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockPrice> priceList = priceSvc.retrievePrices(tickerSymbol);
		LOGGER.info("scan - found {} prices for {}", priceList.size(), tickerSymbol);
		for (StockPrice price: priceList) {
			this.evaluatePrice(price);
		}
	}

}
