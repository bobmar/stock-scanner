package org.rhm.stock.batch;

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

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("priceLoader")
public class PriceLoaderJob implements BatchJob {
	@Autowired
	private TickerService tickerSvc;
	@Autowired
	private PriceService priceSvc;
	@Autowired
	private BatchStatusService batchStatSvc ;
	private final static Logger LOGGER = LoggerFactory.getLogger(PriceLoaderJob.class);
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
		LOGGER.info("processTicker - retrieve prices for {}", tickerSymbol);
		List<StockPrice> priceList = priceSvc.retrieveSourcePrices(tickerSymbol, days);
		LOGGER.debug("processTicker - found {} prices for {}", priceList.size(), tickerSymbol);
		Date mostRecentPriceDate = null;
		if (!priceList.isEmpty()) {
			if (priceSvc.saveStockPrice(priceList) != null) {
				success = true;
				mostRecentPriceDate = findMostRecentPriceDate(priceList);
				LOGGER.info("processTicker - {} saved {} prices", tickerSymbol, priceList.size());
				LOGGER.info("processTicker - {} latest price date: {}", tickerSymbol, mostRecentPriceDate);
			}
			if (oldestAcceptableDate.compareTo(mostRecentPriceDate) > 0) {
				LOGGER.warn("processTicker - ** most recent price is {}/oldest acceptable is {}; will delete ticker", mostRecentPriceDate, oldestAcceptableDate);
				tickerSvc.deleteTicker(tickerSymbol);
			}
		}
		else {
			LOGGER.error("processTicker - {} error during price download", tickerSymbol);
			tickerSvc.deleteTicker(tickerSymbol);
		}
		return success;
	}
	
	private int processTickers(List<StockTicker> tickerList) {
		int status = 100;
		int tickerCnt = 0;
		for (StockTicker ticker: tickerList) {
			if (this.processTicker(ticker.getTickerSymbol())) {
				tickerCnt++;
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
		LOGGER.debug("run - processing " + tickerList.size() + " tickers");
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
