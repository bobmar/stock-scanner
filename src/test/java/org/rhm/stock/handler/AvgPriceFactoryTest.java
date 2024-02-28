package org.rhm.stock.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rhm.stock.domain.AveragePrice;
import org.rhm.stock.domain.StockPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
@SpringBootTest(classes = {StockPriceData.class})
public class AvgPriceFactoryTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(AvgPriceFactoryTest.class);
  @Autowired
  private StockPriceData stockPriceData;

  @Test
  public void calcAvgTest() {
    AveragePrice avgPrice = AvgPriceFactory.calculateAvgPrice(this.generateSimplePrices(), 2);
    Assertions.assertEquals(34.00, avgPrice.getAvgPrice());
    Assertions.assertEquals(10000, avgPrice.getAvgVolume());
  }

  @Test
  public void calcAvgTest2() {
    AveragePrice avgPrice = AvgPriceFactory.calculateAvgPrice(this.generatePrices(), 2);
    Assertions.assertEquals(34.5, avgPrice.getAvgPrice());
    Assertions.assertEquals(15000, avgPrice.getAvgVolume());
  }

  @Test
  public void calcAvgTest3() {
    int[] days = new int[3];
    days[0] = 10;
    days[1] = 20;
    days[2] = 50;
    List<StockPrice> stockPrices = this.stockPriceData.stockPrices();
    LOGGER.info("Stock price list {}", stockPrices.size());
    List<AveragePrice> avgPrices = AvgPriceFactory.calculateAvgPrices(stockPrices, days);
    BigDecimal value = null;
    Assertions.assertEquals(3, avgPrices.size());
    for (AveragePrice avgPrice: avgPrices) {
      switch (avgPrice.getDaysCnt()) {
        case 10:
          value = BigDecimal.valueOf(avgPrice.getAvgPrice());
          value = value.setScale(2, RoundingMode.HALF_UP);
          Assertions.assertEquals(171.24, value.doubleValue());
          Assertions.assertEquals(47375650, avgPrice.getAvgVolume());
          break;
        case 20:
          value = BigDecimal.valueOf(avgPrice.getAvgPrice());
          value = value.setScale(2, RoundingMode.HALF_UP);
          Assertions.assertEquals(169.22, value.doubleValue());
          Assertions.assertEquals(52902970, avgPrice.getAvgVolume());
          break;
        case 50:
          value = BigDecimal.valueOf(avgPrice.getAvgPrice());
          value = value.setScale(2, RoundingMode.HALF_UP);
          Assertions.assertEquals(159.46, value.doubleValue());
          Assertions.assertEquals(48589846, avgPrice.getAvgVolume());
          break;
      }
    }
  }

  private List<StockPrice> generateSimplePrices() {
    List<StockPrice> prices = new ArrayList<>();
    StockPrice price = new StockPrice();
    price.setOpenPrice(34.00);
    price.setHighPrice(34.00);
    price.setLowPrice(34.00);
    price.setClosePrice(34.00);
    price.setVolume(10000L);
    prices.add(price);
    price = new StockPrice();
    price.setOpenPrice(34.00);
    price.setHighPrice(34.00);
    price.setLowPrice(34.00);
    price.setClosePrice(34.00);
    price.setVolume(10000L);
    prices.add(price);
    price = new StockPrice();
    price.setOpenPrice(34.00);
    price.setHighPrice(34.00);
    price.setLowPrice(34.00);
    price.setClosePrice(34.00);
    price.setVolume(10000L);
    prices.add(price);
    return prices;
  }

  private List<StockPrice> generatePrices() {
    List<StockPrice> prices = new ArrayList<>();
    StockPrice price = new StockPrice();
    price.setOpenPrice(34.00);
    price.setHighPrice(34.00);
    price.setLowPrice(34.00);
    price.setClosePrice(34.00);
    price.setVolume(10000L);
    prices.add(price);
    price = new StockPrice();
    price.setOpenPrice(34.00);
    price.setHighPrice(34.00);
    price.setLowPrice(34.00);
    price.setClosePrice(35.00);
    price.setVolume(20000L);
    prices.add(price);
    price = new StockPrice();
    price.setOpenPrice(34.00);
    price.setHighPrice(54.00);
    price.setLowPrice(34.00);
    price.setClosePrice(36.00);
    price.setVolume(30000L);
    prices.add(price);
    return prices;
  }
}
