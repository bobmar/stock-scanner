package org.rhm.stock.batch;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.service.BatchStatusService;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("priceLoader")
public class PriceLoaderJob implements BatchJob {
	@Autowired
	private TickerService tickerSvc = null;
	@Autowired
	private PriceService priceSvc = null;
	@Autowired
	private BatchStatusService batchStatSvc = null;
	private Logger logger = LoggerFactory.getLogger(PriceLoaderJob.class);
	private Date oldestAcceptableDate = null;
	public PriceLoaderJob() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -30);
		oldestAcceptableDate = cal.getTime();
	}
	
	private Date findMostRecentPriceDate(List<StockPrice> priceList) {
		Date mostRecentDate = priceList.stream().sorted((o1,o2)->{return o1.getPriceDate().compareTo(o2.getPriceDate()) * -1;}).collect(Collectors.toList()).get(0).getPriceDate();
		return mostRecentDate;
	}
	
	private boolean processTicker(String tickerSymbol) {
		boolean success = false;
		int days = 366;
		long cnt = priceSvc.priceCount(tickerSymbol);
		if (cnt < days) {
			days = 540;
		}
		logger.info("processTicker - retrieve prices for {}", tickerSymbol);
		List<StockPrice> priceList = priceSvc.retrieveSourcePrices(tickerSymbol, days);
		logger.debug("processTicker - found {} prices for {}", priceList.size(), tickerSymbol);
		Date mostRecentPriceDate = null;
		if (!priceList.isEmpty()) {
			if (priceSvc.saveStockPrice(priceList) != null) {
				success = true;
				mostRecentPriceDate = findMostRecentPriceDate(priceList);
				logger.info("processTicker - {} saved {} prices", tickerSymbol, priceList.size());
				logger.info("processTicker - {} latest price date: {}", tickerSymbol, mostRecentPriceDate);
			}
			if (oldestAcceptableDate.compareTo(mostRecentPriceDate) == 1) {
				logger.warn("processTicker - ** most recent price is " + mostRecentPriceDate + "/oldest acceptable is " + oldestAcceptableDate + "; will delete ticker");
				tickerSvc.deleteTicker(tickerSymbol);
			}
		}
		else {
			logger.error("processTicker - {} error during price download", tickerSymbol);
			tickerSvc.deleteTicker(tickerSymbol);
		}
		return success;
	}
	
	private int processTickers(List<StockTicker> tickerList) {
		int status = 100;
		int tickerCnt = 0;
		int numPerInterval = 30;
		final long interval = 60000;
		long checkPoint = System.currentTimeMillis();
		long timeRemaining = -1;
		for (StockTicker ticker: tickerList) {
			if (this.processTicker(ticker.getTickerSymbol())) {
				tickerCnt++;
				if (tickerCnt >= numPerInterval && tickerCnt % numPerInterval == 0) {
					timeRemaining = System.currentTimeMillis() - checkPoint;
					if (timeRemaining > 0) {
            try {
              Thread.sleep(timeRemaining);
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
					checkPoint = System.currentTimeMillis();
					System.gc();
				}
			}
		}
		if (tickerCnt == tickerList.size()) {
			status = 0;
		}
		return status;
	}
	
	@Override
	public BatchStatus run() {
		BatchStatus status = new BatchStatus(this.getClass());
		List<StockTicker> tickerList = tickerSvc.retrieveTickerList();
		logger.debug("run - processing " + tickerList.size() + " tickers");
		if (this.processTickers(tickerList) == 0) {
			status.setCompletionMsg("Prices loaded successfully - " + tickerList.size() + " tickers processed");
			status.setSuccess(true);
		}
		else {
			status.setCompletionMsg("Price loading failed");
			status.setSuccess(false);
		}
		status.setFinishDate(LocalDateTime.now());
		batchStatSvc.saveBatchStatus(status);
		return status;
	}

}
