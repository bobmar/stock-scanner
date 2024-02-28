package org.rhm.stock.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rhm.stock.domain.StockPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Map;

@SpringBootTest(classes = {StockPriceData.class})
public class StockPriceDataTest {
    @Autowired
    private StockPriceData testData;

    @Test
    public void loadTestData() {
        List<StockPrice> prices = testData.stockPrices();
        Assertions.assertEquals(50, prices.size());
    }

    @Test
    public void dateFormat() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = dateFormat.parse("2024-02-27T07:00:00.000");
        Assertions.assertNotNull(date);
    }
}
