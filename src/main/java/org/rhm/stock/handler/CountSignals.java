package org.rhm.stock.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.rhm.stock.domain.SignalType;
import org.rhm.stock.domain.SignalTypeCount;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.service.SignalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountSignals {
	@Autowired
	private SignalService signalSvc = null;
	private Map<String, SignalTypeCount> stCountMap = new HashMap<String,SignalTypeCount>();
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private Logger logger = LoggerFactory.getLogger(CountSignals.class);
	
	private String createKey(StockSignal signal) {
		String key = signal.getSignalType() + ":" + df.format(signal.getPriceDate());
		return key;
	}

	private void processSignal(StockSignal signal, String sigDesc) {
		String sigCntKey = this.createKey(signal);
		SignalTypeCount sigCnt = stCountMap.get(sigCntKey);
		if (sigCnt == null) {
			sigCnt = new SignalTypeCount();
			sigCnt.setSignalCode(signal.getSignalType());
			sigCnt.setSignalDate(df.format(signal.getPriceDate()));
			sigCnt.setSignalCountKey(sigCntKey);
			sigCnt.setSignalDesc(sigDesc);
			stCountMap.put(sigCntKey, sigCnt);
		}
		sigCnt.incrementSignal();
	}
	
	private void processSignals(List<StockSignal> signalList, String sigDesc) {
		for (StockSignal signal: signalList) {
			this.processSignal(signal, sigDesc);
		}
	}
	
	private void processSignalTypes(List<SignalType> signalTypeList) {
		List<StockSignal> signalList = null;
		for (SignalType st: signalTypeList) {
			logger.info("processSignalTypes - count signals: " + st.getSignalCode() + "/" + st.getSignalDesc());
			signalList = signalSvc.findSignalsUnsorted(st.getSignalCode());
			this.processSignals(signalList, st.getSignalDesc());
		}
		this.saveSignalCounts();
	}
	
	private void saveSignalCounts() {
		signalSvc.saveSignalCounts(stCountMap.values().stream().collect(Collectors.toList()));
		for (SignalTypeCount stc: stCountMap.values()) {
			logger.info("saveSignalCounts - " + stc.getSignalCountKey() + "|" + stc.getSignalDesc() + ": " + stc.getSignalCount());
		}
	}
	
	public void run() {
		List<SignalType> signalTypeList = signalSvc.signalTypes();
		logger.info("run - found " + signalTypeList.size() + " signals");
		this.processSignalTypes(signalTypeList);
	}
}
