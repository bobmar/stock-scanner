package org.rhm.stock.handler.signal;

import java.util.List;

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
@Qualifier("upDownVol")
public class UpDownVol implements SignalScanner {
	@Autowired
	private SignalService sigSvc = null;
	@Autowired
	private StatisticService statSvc = null;
	@Autowired
	private PriceService priceSvc = null;
	private Logger logger = LoggerFactory.getLogger(UpDownVol.class);
	private static final String UPDN_INC_SIGNAL = "UPDNVOLINCR";
	private static final String UPD_3_4_SIGNAL = "UPDNVOL3_4";
	private static final String UPD_4_5_SIGNAL = "UPDNVOL4_5";
	private static final String UPD_5_6_SIGNAL = "UPDNVOL5_6";
	private static final String UPD_GT1_SIGNAL = "UPDNVOLGT1";
	
	private void detectIncrease(List<StockStatistic> statList) {
		StockStatistic currStat = null, prevStat = null;
		if (statList.size() > 1) {
			currStat = statList.get(0);
			prevStat = statList.get(1);
			if (currStat.getStatisticValue().compareTo(prevStat.getStatisticValue()) == 1) {
				sigSvc.createSignal(new StockSignal(priceSvc.findStockPrice(currStat.getPriceId()), UPDN_INC_SIGNAL));
			}
			checkRange(currStat);
		}
	}
	
	private void checkRange(StockStatistic stat) {
		StockSignal signal = null;
		if (stat.getStatisticValue().doubleValue() > 1.0) {
			signal = new StockSignal(priceSvc.findStockPrice(stat.getPriceId()), UPD_GT1_SIGNAL);
		}
		if (stat.getStatisticValue().doubleValue() >= 3.0 && stat.getStatisticValue().doubleValue() < 4.0) {
			signal = new StockSignal(priceSvc.findStockPrice(stat.getPriceId()), UPD_3_4_SIGNAL);
		}
		else {
			if (stat.getStatisticValue().doubleValue() >= 4.0 && stat.getStatisticValue().doubleValue() < 5.0) {
				signal = new StockSignal(priceSvc.findStockPrice(stat.getPriceId()), UPD_4_5_SIGNAL);
			}
			else {
				if (stat.getStatisticValue().doubleValue() >= 5.0 && stat.getStatisticValue().doubleValue() < 6.0) {
					signal = new StockSignal(priceSvc.findStockPrice(stat.getPriceId()), UPD_5_6_SIGNAL);
				}
			}
		}
		if (signal != null) {
			sigSvc.createSignal(signal);
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockStatistic> statList = statSvc.retrieveStatList(tickerSymbol, "UPDNVOL50");
		logger.info("scan - found " + statList.size() + " UPDNVOL50 stats for " + tickerSymbol);
		while (statList.size() > 2) {
			this.detectIncrease(statList);
			statList.remove(0);
		}
	}

}
