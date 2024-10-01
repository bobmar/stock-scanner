package org.rhm.stock.handler.stat;

import java.util.List;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("upDownVolCalc")
public class UpDownVolume implements StatisticCalculator {
	@Autowired
	private StatisticService statSvc;
	private static final String UP_DOWN_VOL_50 = "UPDNVOL50";
	private final Logger LOGGER = LoggerFactory.getLogger(UpDownVolume.class);
	
	private void calcUpDownRatio(List<StockPrice> priceList) {
		double upVolume = 0, downVolume = 0;
		double upDownRatio = 0.0;
		double priceDiff = 0.0;
		StockPrice currPrice = null, prevPrice = null, firstPrice = null;
		firstPrice = priceList.get(0);
		for (int i = 0; i < 49; i++) {
			currPrice = priceList.get(i);
			prevPrice = priceList.get(i+1);
			priceDiff = currPrice.getClosePrice() - prevPrice.getClosePrice();
			LOGGER.debug("calcUpDownRatio - " + currPrice.getTickerSymbol() + " curr price=" + currPrice.getClosePrice() + "; prev price=" + prevPrice.getClosePrice() + "; diff=" + priceDiff);
			if (priceDiff >= 0) {
				upVolume += currPrice.getVolume();
				LOGGER.debug("calcUpDownRatio - up volume=" + upVolume);
			}
			else {
				downVolume += currPrice.getVolume();
				LOGGER.debug("calcUpDownRatio - down volume=" + downVolume);
			}
		}
		LOGGER.debug("calcUpDownRatio - " + currPrice.getTickerSymbol() + " up volume=" + upVolume + "/ down volume=" + downVolume);
		if (downVolume > 0) {
			upDownRatio = upVolume / downVolume;
			LOGGER.debug("calcUpDownRatio - up/down ratio=" + upDownRatio);
			statSvc.createStatistic(
				new StockStatistic(firstPrice.getPriceId(), UP_DOWN_VOL_50, upDownRatio, firstPrice.getTickerSymbol(), firstPrice.getPriceDate())
				,false);
		}
	}
	
	@Override
	public void calculate(List<StockPrice> priceList) {
		LOGGER.info("calculate - processing " + priceList.size() + " prices for " + priceList.get(0).getTickerSymbol());
		while (priceList.size() > 51) {
			LOGGER.debug("calculate - price list contains " + priceList.size() + " entries");
			this.calcUpDownRatio(priceList.subList(0, 50));
			priceList.remove(0);
		}
	}

}
