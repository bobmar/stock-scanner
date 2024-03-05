package org.rhm.stock.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rhm.stock.dto.PriceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {AlphaVantageDownload.class})
public class PriceDownloadTest {
  @Autowired
  private AlphaVantageDownload priceDownload;

  @Test
  public void priceDownloadTest() {
    List<PriceBean> result = priceDownload.downloadPrices("SMCI");
    Assertions.assertEquals(100, result.size());
  }

  @Test
  public void fullDownloadTest() {
    List<PriceBean> result = priceDownload.downloadPrices("ALRM", AlphaVantageDownload.FORMAT_FULL);
  }

  @Test
  public void downloadntest() {
    List<PriceBean> result = priceDownload.downloadPrices("ALRM", "10");
  }
}
