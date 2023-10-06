package org.rhm.stock.handler.signal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.service.SignalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("signalCombo")
public class SignalCombo implements SignalScanner {
	@Autowired
	private SignalService signalSvc = null;
	private static final String CONFIRM_BUY_SETUP = "SEQUENTIAL";
	private static final String MOMENTUM_CANDIDATE = "MOMENTUM";
	private static final String SIGNAL_MATCH = "|UPTREND|UPDNVOLINCR|AVGVOL500K|AVG20ABV200|";
	private Logger logger = LoggerFactory.getLogger(SignalCombo.class);

	private StockSignal cloneSignal(StockSignal signal) {
		return cloneSignal(signal, CONFIRM_BUY_SETUP);
	}

	private StockSignal cloneSignal(StockSignal signal, String newSignalType) {
		StockSignal newSignal = new StockSignal(signal);
		newSignal.setSignalType(newSignalType);
		newSignal.setSignalId(signal.getPriceId() + ":" + newSignalType);
		return newSignal;
	}

	private void evaluateTdSequential(List<StockSignal> signalList) {
		StockSignal firstSignal = signalList.get(0);
		if (firstSignal.getSignalType().equals("BUYSETUP") || firstSignal.getSignalType().equals("PBUYSETUP")) {
			for (int i = 1; i < 4; i++) {
				if (signalList.get(i).getSignalType().equals("BEARFLIP")) {
					signalSvc.createSignal(this.cloneSignal(firstSignal));
					break;
				}
			}
		}
	}
	
	private Map<String,List<StockSignal>> createSignalMap(List<StockSignal> signalList) {
		Map<String,List<StockSignal>> signalMap = new HashMap<String,List<StockSignal>>();
		List<StockSignal> singleDayList = null;
		for (StockSignal signal: signalList) {
			singleDayList = signalMap.get(signal.getPriceId());
			if (singleDayList == null) {
				singleDayList = new ArrayList<StockSignal>();
				signalMap.put(signal.getPriceId(), singleDayList);
			}
			singleDayList.add(signal);
		}
		return signalMap;
	}
	
	private boolean isCandidate(List<StockSignal> signalList) {
		int matchCnt = 0;
		for (StockSignal signal: signalList) {
			if (SIGNAL_MATCH.contains(signal.getSignalType())) {
				matchCnt++;
			}
		}
		return matchCnt == 4;
	}
	
	private void findMomentumCandidate(List<StockSignal> signalList) {
		Map<String,List<StockSignal>> signalMap = this.createSignalMap(signalList);
		Iterator<String> iter = signalMap.keySet().iterator();
		List<StockSignal> singleDayList = null;
		String priceDate = null;
		while (iter.hasNext()) {
			priceDate = iter.next();
			singleDayList = signalMap.get(priceDate);
			if (this.isCandidate(singleDayList)) {
				signalSvc.createSignal(this.cloneSignal(singleDayList.get(0), MOMENTUM_CANDIDATE));
			}
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<StockSignal> signalList = signalSvc.findSignalsByTicker(tickerSymbol);
		logger.info("scan - found " + signalList.size() + " signals for " + tickerSymbol);
		this.findMomentumCandidate(signalList);
		while (signalList.size() > 5) {
			this.evaluateTdSequential(signalList);
			signalList.remove(0);
		}
	}

}
