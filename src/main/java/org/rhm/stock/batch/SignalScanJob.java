package org.rhm.stock.batch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.handler.signal.SignalScanner;
import org.rhm.stock.service.BatchStatusService;
import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("signalScan")
public class SignalScanJob implements BatchJob {
	@Autowired
	@Qualifier("priceBreakout")
	private SignalScanner priceBreakout = null;
	@Autowired
	@Qualifier("priceGap")
	private SignalScanner priceGap = null;
	@Autowired
	@Qualifier("closeNearHigh")
	private SignalScanner closeNearHigh = null;
	@Autowired
	@Qualifier("priceTrend")
	private SignalScanner priceTrend = null;
	@Autowired
	@Qualifier("stdDeviation")
	private SignalScanner stdDeviation = null;
	@Autowired
	@Qualifier("dailyVsAvgCrossover")
	private SignalScanner dlyVsAvgCrossover = null;
	@Autowired
	@Qualifier("deMark")
	private SignalScanner deMark = null;
	@Autowired
	@Qualifier("signalCombo")
	private SignalScanner signalCombo = null;
	@Autowired
	@Qualifier("upDownVol")
	private SignalScanner upDownVol = null;
	@Autowired
	@Qualifier("highLowScan")
	private SignalScanner highLowScan = null;
	@Autowired
	@Qualifier("ibdScanner")
	private SignalScanner ibdScanner = null;
	@Autowired
	@Qualifier("momentumScan")
	private SignalScanner momentum = null;
	
	@Autowired
	private TickerService tickerSvc = null;
	@Autowired
	private BatchStatusService batchStatSvc = null;
	private Logger logger = LoggerFactory.getLogger(SignalScanJob.class);
	private List<SignalScanner> scannerList = null;
	
	private List<SignalScanner> scannerList() {
		List<SignalScanner> scannerList = new ArrayList<SignalScanner>();
		scannerList.add(priceBreakout);
		scannerList.add(priceGap);
		scannerList.add(closeNearHigh);
		scannerList.add(priceTrend);
		scannerList.add(stdDeviation);
		scannerList.add(dlyVsAvgCrossover);
		scannerList.add(deMark);
		scannerList.add(upDownVol);
		scannerList.add(highLowScan);
		scannerList.add(ibdScanner);
		scannerList.add(momentum);
		//signalCombo should always be added last
		scannerList.add(signalCombo);
		logger.debug("scannerList - loaded " + scannerList.size() + " signal scanners");
		return scannerList;
	}
	
	@PostConstruct
	private void init() {
		this.scannerList = this.scannerList();
	}
	
	@Override
	public BatchStatus run() {
		List<StockTicker> tickerList = tickerSvc.retrieveTickerList();
		BatchStatus batchStatus = new BatchStatus(this.getClass());
		for (StockTicker ticker: tickerList) {
			for (SignalScanner scanner: scannerList) {
				logger.info("run - processing ticker " + ticker.getTickerSymbol());
				scanner.scan(ticker.getTickerSymbol());
			}
		}
		batchStatus.setCompletionMsg("Successful completion - processed " + tickerList.size() + " tickers");
		batchStatus.setSuccess(true);
		batchStatus.setFinishDate(LocalDateTime.now());
		batchStatSvc.saveBatchStatus(batchStatus);
		return batchStatus;
	}

}
