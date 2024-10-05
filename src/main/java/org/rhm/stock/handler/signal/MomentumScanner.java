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
@Qualifier("momentumScan")
public class MomentumScanner implements SignalScanner {
	@Autowired
	private SignalService signalSvc;
	@Autowired
	private StatisticService statSvc;
	@Autowired
	private PriceService priceSvc;
	
	private static final String STAT_ZSCORE = "ZSCORE";
	private static final String STAT_TRMOM = "TRMOM";
	private static final String SIGNAL_ZSPOS_5DAYS = "ZSPOS5DY";
	private static final String SIGNAL_ZSNEG_5DAYS = "ZSNEG5DY";
	private static final String SIGNAL_TRMPOS_5DAYS = "TRMPOS5DY";
	private static final String SIGNAL_TRMNEG_5DAYS = "TRMNEG5DY";
	private static final String SIGNAL_ZS_XOVER = "ZSXOVER";
	private static final String SIGNAL_TR_XOVER = "TRXOVER";
	private static final int DAYS_TO_CONSIDER = 5;
	private Map<String,StockPrice> priceMap = new HashMap<String,StockPrice>();
	private static final Logger LOGGER = LoggerFactory.getLogger(MomentumScanner.class);
	
	private StockPrice findStockPrice(String priceId) {
		StockPrice price = priceMap.get(priceId);
		if (price == null) {
			price = priceSvc.findStockPrice(priceId);
			priceMap.put(priceId, price);
		}
		return price;
	}
	
	private void evaluateMomentum(List<StockStatistic> statList) {
		int posCnt = 0, negCnt = 0;
		StockStatistic stat = null;
		if (statList.size() > DAYS_TO_CONSIDER) {
			for (int i = 0; i < DAYS_TO_CONSIDER; i++) {
				if (statList.get(i).getStatisticValue() > 0) {
					posCnt++;
				}
				else {
					if (statList.get(i).getStatisticValue() < 0) {
						negCnt++;
					}
				}
			}

			if (posCnt == DAYS_TO_CONSIDER) {
				stat = statList.get(0);
				signalSvc.createSignal(new StockSignal(this.findStockPrice(stat.getPriceId()), stat.getStatisticType().equals(STAT_ZSCORE)?SIGNAL_ZSPOS_5DAYS:SIGNAL_TRMPOS_5DAYS));
			}
			else {
				stat = statList.get(0);
				if (negCnt == DAYS_TO_CONSIDER) {
					signalSvc.createSignal(new StockSignal(this.findStockPrice(stat.getPriceId()), stat.getStatisticType().equals(STAT_ZSCORE)?SIGNAL_ZSNEG_5DAYS:SIGNAL_TRMNEG_5DAYS));
				}
			}
		}
	}
	
	private void evaluateCrossOver(StockStatistic currStat, StockStatistic prevStat) {
		if (prevStat.getStatisticValue() < 0) {
			if (currStat.getStatisticValue() > 0) {
				signalSvc.createSignal(new StockSignal(this.findStockPrice(currStat.getPriceId()), currStat.getStatisticType().equals(STAT_ZSCORE)?SIGNAL_ZS_XOVER:SIGNAL_TR_XOVER));				
			}
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockStatistic> zScoreList = statSvc.retrieveStatList(tickerSymbol, STAT_ZSCORE);
		List<StockStatistic> trMomList = statSvc.retrieveStatList(tickerSymbol, STAT_TRMOM);
		LOGGER.info("scan - scanning momentum scores for {}", tickerSymbol);
		while (zScoreList.size() > DAYS_TO_CONSIDER) {
			this.evaluateMomentum(zScoreList);
			this.evaluateCrossOver(zScoreList.get(0), zScoreList.get(1));
			zScoreList.remove(0);
		}
		while (trMomList.size() > DAYS_TO_CONSIDER) {
			this.evaluateMomentum(trMomList);
			this.evaluateCrossOver(trMomList.get(0), trMomList.get(1));
			trMomList.remove(0);
		}
	}

}
