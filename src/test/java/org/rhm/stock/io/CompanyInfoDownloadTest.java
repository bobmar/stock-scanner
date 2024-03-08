package org.rhm.stock.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rhm.stock.dto.PriceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {CompanyInfoDownload.class})
public class CompanyInfoDownloadTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyInfoDownload.class);
    @Autowired
    CompanyInfoDownload coInfo;

    @Test
    public void profileTest() throws IOException, InterruptedException {
        Map<String,Object> profile = this.coInfo.retrieveCompanyInfo("SMCI");
        LOGGER.info(profile.toString());
    }

    @Test
    public void priceDownloadTest() {
        List<PriceBean> prices = coInfo.downloadPrices("SMCI", 10);
        Assertions.assertEquals(9, prices.size());
    }
}
