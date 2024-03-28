package org.rhm.stock.batch;

import org.rhm.stock.domain.FinancialRatio;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.service.BatchStatusService;
import org.rhm.stock.service.FinancialRatioService;
import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Qualifier("financialRatioLoader")
public class FinancialRatioLoader implements BatchJob {
  private final static Logger LOGGER = LoggerFactory.getLogger(FinancialRatioLoader.class);
  @Autowired
  private FinancialRatioService ratioService;
  @Autowired
  private TickerService tickerService;
  @Autowired
  private BatchStatusService batchStatSvc;
  private int processTicker(String tickerSymbol) {
    List<FinancialRatio> ratios = this.ratioService.downloadRatios(tickerSymbol);
    if (!ratios.isEmpty()) {
      LOGGER.info("processTicker - saving ratios for {}", tickerSymbol);
      ratioService.save(ratios);
    }
    return ratios.size();
  }

  private int processTickers() {
    List<StockTicker> tickers = this.tickerService.retrieveTickerList();
    int ratiosSaved = 0;
    for (StockTicker ticker: tickers) {
      ratiosSaved += this.processTicker(ticker.getTickerSymbol());
    }
    return ratiosSaved;
  }

  @Override
  public BatchStatus run() {
    BatchStatus status = new BatchStatus(FinancialRatioLoader.class);
    int ratiosSaved = this.processTickers();
    status.setCompletionMsg(String.format("Saved %s financial ratios", ratiosSaved));
    status.setFinishDate(LocalDateTime.now());
    status.setSuccess(Boolean.TRUE);
    batchStatSvc.saveBatchStatus(status);
    return status;
  }
}
