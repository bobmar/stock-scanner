package org.rhm.stock.handler.stat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;

import org.rhm.stock.domain.AveragePrice;
import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.service.AveragePriceService;
import org.rhm.stock.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {DailyPriceVsAvg.class})
public class DailyPriceVsAvgTest {
  private static final String DLY_PRC_VS_20_DAY_AVG = "DYPRCV20A";
  @Autowired
  private DailyPriceVsAvg dlyPriceVsAvg;

  @MockBean
  private AveragePriceService avgPriceSvc;

  @MockBean
  private StatisticService statSvc;

  @BeforeEach
  public void mockPrep() {
    Mockito.when(avgPriceSvc.findAvgPriceList(anyString())).thenReturn(this.avgPriceList());
    Mockito.when(statSvc.createStatistic(any(StockStatistic.class))).thenReturn(new StockStatistic());
  }

  @Test
  public void calculatePriceVsAvgTest() {
    StockPrice price = new StockPrice();
    price.setPriceId("ZZZ:2023-12-24");
    price.setTickerSymbol("ZZZ");
    price.setClosePrice(100.0);
    AveragePrice avgPrice = new AveragePrice();
    avgPrice.setAvgPrice(100.0);
    StockStatistic stat = dlyPriceVsAvg.calcPriceVsAvg(price, 20, DLY_PRC_VS_20_DAY_AVG, avgPrice);
    Assertions.assertEquals(1.0, stat.getStatisticValue());
    Assertions.assertEquals(String.format("%s:%s", price.getPriceId(), DLY_PRC_VS_20_DAY_AVG), stat.getStatId());
    Assertions.assertNotNull(stat);
  }

  private List<StockAveragePrice> avgPriceList() {
    List<StockAveragePrice> averagePrices = new ArrayList<>();
    StockAveragePrice avgPrice = new StockAveragePrice();
    List<AveragePrice> avgList = new ArrayList<>();
    avgPrice.setAvgList(avgList);
    avgPrice.setAvgList(avgList);
    averagePrices.add(avgPrice);
    return averagePrices;
  }

  private List<StockPrice> priceList() {
    List<StockPrice> prices = new ArrayList<>();
    StockPrice price = new StockPrice();
    prices.add(price);
    return prices;
  }

}
