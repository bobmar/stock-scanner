package org.rhm.stock.batch;

import org.rhm.stock.domain.FinancialGrowth;
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
@Qualifier("financialGrowthLoader")
public class FinancialGrowthLoader implements BatchJob {
  private final static Logger LOGGER = LoggerFactory.getLogger(FinancialGrowthLoader.class);
  @Autowired
  private FinancialRatioService finModelService;
  @Autowired
  private TickerService tickerService;
  @Autowired
  private BatchStatusService batchStatSvc;
  private int processTicker(String tickerSymbol) {
    List<FinancialGrowth> financialGrowthList = this.finModelService.downloadFinancialGrowth(tickerSymbol);
    if (!financialGrowthList.isEmpty()) {
      LOGGER.info("processTicker - saving financial growth for {}", tickerSymbol);
      finModelService.saveGrowthList(financialGrowthList);
    }
    return financialGrowthList.size();
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
    BatchStatus status = new BatchStatus(FinancialGrowthLoader.class);
    int recordsSaved = this.processTickers();
    status.setCompletionMsg(String.format("Saved %s financial growth entries", recordsSaved));
    status.setFinishDate(LocalDateTime.now());
    status.setSuccess(Boolean.TRUE);
    batchStatSvc.saveBatchStatus(status);
    return status;
  }
}
