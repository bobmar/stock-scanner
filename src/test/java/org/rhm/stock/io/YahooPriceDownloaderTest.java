package org.rhm.stock.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rhm.stock.dto.PriceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {YahooPriceDownloader.class})
public class YahooPriceDownloaderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(YahooPriceDownloaderTest.class);
    private static final String TICKER_SYMBOL = "SMCI";
    @Autowired
    private YahooPriceDownloader priceDownloader;

    @Test
    public void retrievePriceDataTest() {
        List<PriceBean> prices = priceDownloader.retrievePriceData(TICKER_SYMBOL, 5);
        Assertions.assertFalse(prices.isEmpty());
        LOGGER.info(prices.toString());
    }
}
