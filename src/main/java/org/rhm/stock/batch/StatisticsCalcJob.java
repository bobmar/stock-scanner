package org.rhm.stock.batch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.handler.stat.StatisticCalculator;
import org.rhm.stock.service.BatchStatusService;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("statsCalcJob")
public class StatisticsCalcJob implements BatchJob {
	
	@Autowired
	@Qualifier("priceChgCalc")
	private StatisticCalculator priceChgCalc = null;

	@Autowired
	@Qualifier("highLowPriceCalc")
	private StatisticCalculator highLowPriceCalc = null;
	
	@Autowired
	@Qualifier("upDownVolCalc")
	private StatisticCalculator upDownVolCalc = null;
	
	@Autowired
	@Qualifier("stdDeviation")
	private StatisticCalculator stdDeviation = null;
	
	@Autowired
	@Qualifier("dailyPriceVsAvg")
	private StatisticCalculator dailyPriceVsAvg = null;
	
	@Autowired
	@Qualifier("momentum")
	private StatisticCalculator momentum = null;
	
	@Autowired
	private PriceService priceSvc = null;
	@Autowired
	private TickerService tickerSvc = null;
	@Autowired
	private BatchStatusService batchStatSvc = null;
	
	private List<StatisticCalculator> calcList = null;
	private Logger logger = LoggerFactory.getLogger(StatisticsCalcJob.class);

	@PostConstruct
	public void init() {
		this.calcList = this.calcList();
	}
	
	private List<StatisticCalculator> calcList() {
		List<StatisticCalculator> calcList = new ArrayList<StatisticCalculator>();
		calcList.add(priceChgCalc);
		calcList.add(highLowPriceCalc);
		calcList.add(upDownVolCalc);
		calcList.add(stdDeviation);
		calcList.add(dailyPriceVsAvg);
		calcList.add(momentum);
		return calcList;
	}

	private void processPriceList(List<StockPrice> priceList) {
		List<StockPrice> workingList = new ArrayList<StockPrice>();
		for (StatisticCalculator calc: calcList) {
			workingList.clear();
//			priceList.forEach(workingList::add);
			workingList.addAll(priceList);
			if (workingList.size() > 0) {
				calc.calculate(workingList);
			}
		}
	}
	
	private int processTicker(String tickerSymbol) {
		List<StockPrice> priceList = this.priceSvc.retrievePrices(tickerSymbol);
		logger.debug("processTicker - retrieved " + priceList.size() + " prices for " + tickerSymbol);
		this.processPriceList(priceList);
		return 0;
	}
	
	@Override
	public BatchStatus run() {
		BatchStatus status = new BatchStatus(this.getClass());
		List<StockTicker> tickerList = this.tickerSvc.retrieveTickerList();
		logger.info("run - processing " + tickerList.size() + " tickers");
		int processedCnt = 0;
		for (StockTicker ticker: tickerList) {
			this.processTicker(ticker.getTickerSymbol());
			processedCnt++;
			logger.info("run - " + processedCnt + ") " + ticker.getTickerSymbol() + " processed" );
		}
		status.setCompletionMsg("Processed " + processedCnt + " tickers");
		status.setFinishDate(LocalDateTime.now());
		status.setSuccess(true);
		batchStatSvc.saveBatchStatus(status);
		return status;
	}

}
