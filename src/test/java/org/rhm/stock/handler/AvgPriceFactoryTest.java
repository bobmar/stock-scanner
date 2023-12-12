package org.rhm.stock.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rhm.stock.domain.AveragePrice;
import org.rhm.stock.domain.StockPrice;

import java.util.ArrayList;
import java.util.List;

public class AvgPriceFactoryTest {
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
