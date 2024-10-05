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
@Qualifier("priceBreakout")
public class PriceBreakout implements SignalScanner {
	@Autowired
	private PriceService priceSvc;
	@Autowired
	private StatisticService statSvc;
	@Autowired
	private SignalService signalSvc;
	private static final String FOUR_WK_HIGH = "HIPR4WK";
	private static final String ELEVEN_WK_HIGH = "HIPR11WK";
	private static final String FOUR_WK_BRK_SIG = "4WKBRK";
	private static final String ELEVEN_WK_BRK_SIG = "11WKBRK";
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceBreakout.class);
	
	private StockStatistic findStat(String statId, List<StockStatistic> statList) {
		StockStatistic stockStat = null;
		for (int i = 0; i < statList.size(); i++) {
			if (statList.get(i).getStatId().equals(statId)) {
				if (i < (statList.size() - 1)) {
					stockStat = statList.get(i + 1);
					LOGGER.debug("findStat - compare to stat " + stockStat.getPriceId() + "/" + stockStat.getStatisticValue());
				}
				break;
			}
		}
		return stockStat;
	}
	
	private void evaluatePrice(StockPrice price, List<StockStatistic> statList, String statCode, String signalCode) {
		String statId = price.getPriceId() + ":" + statCode;
		StockStatistic stat = this.findStat(statId, statList);
		if (stat != null) {
			if (price.getHighPrice() > stat.getStatisticValue()) {
				signalSvc.createSignal(new StockSignal(price, signalCode));
				LOGGER.debug("evaluatePrice - create new signal for {}", price.getPriceId());
			}
		}
		else {
			LOGGER.debug("evaluatePrice - statistic not found for {}", statId);
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockStatistic> statList = statSvc.retrieveStatList(tickerSymbol, FOUR_WK_HIGH);
		LOGGER.info("scan - found {} {} statistics for {}", statList.size(), FOUR_WK_HIGH, tickerSymbol);
		List<StockPrice> priceList = priceSvc.retrievePrices(tickerSymbol);
		for (StockPrice price: priceList) {
			this.evaluatePrice(price, statList, FOUR_WK_HIGH, FOUR_WK_BRK_SIG);
		}
		statList = statSvc.retrieveStatList(tickerSymbol, ELEVEN_WK_HIGH);
		LOGGER.info("scan - found {} {} statistics for {}", statList.size(), ELEVEN_WK_HIGH, tickerSymbol);
		for (StockPrice price: priceList) {
			this.evaluatePrice(price, statList, ELEVEN_WK_HIGH, ELEVEN_WK_BRK_SIG);
		}
	}

}
