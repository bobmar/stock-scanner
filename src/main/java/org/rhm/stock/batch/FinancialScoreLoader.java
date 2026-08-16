package org.rhm.stock.batch;

import org.rhm.stock.domain.FinancialGrowth;
import org.rhm.stock.domain.FinancialScore;
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
@Qualifier("financialScoreLoader")
public class FinancialScoreLoader implements BatchJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialScoreLoader.class);
    @Autowired
    private TickerService tickerService;
    @Autowired
    private BatchStatusService batchStatSvc;
    @Autowired
    private FinancialRatioService finModelService;

    private int processTicker(String tickerSymbol) {
        List<FinancialScore> financialScoreList = this.finModelService.downloadFinancialScore(tickerSymbol);
        if (!financialScoreList.isEmpty()) {
            LOGGER.info("processTicker - saving financial score for {}", tickerSymbol);
            finModelService.saveScoreList(financialScoreList);
        }
        return financialScoreList.size();

    }

    private int processTickers() {
        List<StockTicker> tickers = this.tickerService.retrieveTickerList();
        int scoresSaved = 0;
        for (StockTicker ticker: tickers) {
            scoresSaved += this.processTicker(ticker.getTickerSymbol());
        }
        return scoresSaved;

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
