package org.rhm.stock.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.rhm.stock.domain.StockPrice;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class StockPriceData {
    private ObjectMapper mapper = new ObjectMapper();

    public StockPriceData() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }
    public List<StockPrice> stockPrices() {
        List<StockPrice> priceList = null;
        try {
            File file = new File("./src/test/java/org/rhm/stock/handler/stockPrice_amzn.json");
            priceList = Arrays.asList(
                    mapper.readValue(file, StockPrice[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return priceList;
    }
}
