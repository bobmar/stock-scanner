package org.rhm.stock.batch;

import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.service.AveragePriceService;
import org.rhm.stock.service.BatchStatusService;
import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Qualifier("emaPriceLoader")
public class EmaPriceLoader implements BatchJob {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmaPriceLoader.class);
  @Autowired
  private TickerService tickerSvc = null;
  @Autowired
  private AveragePriceService avgPriceSvc = null;
  @Autowired
  private BatchStatusService batchStatSvc = null;

  private int processTicker(String tickerSymbol) {
    LOGGER.info("processTicker - {}", tickerSymbol);
    List<StockAveragePrice> updatedList = this.avgPriceSvc.addEmaToAvgBal(tickerSymbol);
    if (!updatedList.isEmpty()) {
      this.avgPriceSvc.saveAll(updatedList);
      LOGGER.info("processTicker - found {} prices", updatedList.size());
    }
    return updatedList.size();
  }

  private int process(List<StockTicker> tickers) {
    int updatedPriceCnt = 0;
    for (StockTicker ticker: tickers) {
      updatedPriceCnt += this.processTicker(ticker.getTickerSymbol());
    }
    return updatedPriceCnt;
  }

  @Override
  public BatchStatus run() {
    BatchStatus status = new BatchStatus(FinancialRatioLoader.class);
    List<StockTicker> tickers = tickerSvc.retrieveTickerList();
    int updatedPrices = this.process(tickers);
    LOGGER.info("run - added EMA to {} prices", updatedPrices);
    status.setFinishDate(LocalDateTime.now());
    status.setSuccess(Boolean.TRUE);
    status.setCompletionMsg(String.format("EMA update completed; updated %s prices", updatedPrices));
    batchStatSvc.saveBatchStatus(status);
    return status;
  }
}
