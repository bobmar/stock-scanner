package org.rhm.stock.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.cboe.com/available_weeklys/get_csv_download/
 */
@Component
public class CboeWeekly {
    @Value(value = "${cboe.options.url}")
    private String cboeOptionsUrl;
    private final static Logger LOGGER = LoggerFactory.getLogger(CboeWeekly.class);
    public List<String> retrieveWeeklyOptionStocks() {
        List<String> weeklyOptions = new ArrayList<>();
        try {
            URL url = new URL(cboeOptionsUrl);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(",") > 0) {
                    LOGGER.info(line.substring(1, line.indexOf(",")-1));
                    weeklyOptions.add(line.substring(1, line.indexOf(",")-1));
                }
                else {
                    LOGGER.warn(line);
                }
            }
            is.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return weeklyOptions;
    }
}
