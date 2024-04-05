package org.rhm.stock.batch;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rhm.stock.domain.FinancialRatio;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.service.FinancialRatioService;
import org.rhm.stock.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {FinancialRatioLoader.class})
public class FinancialRatioLoaderTest {
  @Autowired
  FinancialRatioLoader ratioLoader;

  @MockBean
  FinancialRatioService financialRatioService;
  @MockBean
  TickerService tickerService;

  @Test
  public void loaderTest() {
    when(financialRatioService.downloadRatios(anyString())).thenReturn(this.createFinancialRatioList());
    doNothing().when(financialRatioService).saveRatios(anyList());
    when(tickerService.retrieveTickerList()).thenReturn(this.createTickerList());
    BatchStatus status = ratioLoader.run();
    Assertions.assertTrue(status.getSuccess());
  }

  private List<FinancialRatio> createFinancialRatioList() {
    List<FinancialRatio> ratios = new ArrayList<>();
    FinancialRatio ratio = new FinancialRatio();
    ratio.setSymbol("ABCD");
    ratio.setDate("2024-03-06");
    ratio.setFinRatioId(String.format("%s:%s", ratio.getSymbol(), ratio.getDate()));
    ratios.add(ratio);
    return ratios;
  }

  private List<StockTicker> createTickerList() {
    List<StockTicker> tickers = new ArrayList<>();
    StockTicker ticker = new StockTicker();
    ticker.setTickerSymbol("ABCD");
    ticker.setCompanyName("ABCD Company");
    tickers.add(ticker);
    return tickers;
  }

}
