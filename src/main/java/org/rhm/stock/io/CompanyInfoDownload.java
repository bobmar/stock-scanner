package org.rhm.stock.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rhm.stock.domain.FinancialRatio;
import org.rhm.stock.dto.PriceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Qualifier("fmpDownload")
public class CompanyInfoDownload implements DataDownload {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyInfoDownload.class);
    @Value(value = "${data.fmp.apikey}")
    private String apiKey;
    @Value(value = "${company.download.baseurl}")
    private String baseUrl;
    @Value(value = "${company.download.profile}")
    private String profileUri;
    @Value(value = "${company.download.ratios}")
    private String ratiosUri;
    @Value(value = "${company.download.prices}")
    private String pricesUri;
    private final ObjectMapper mapper = new ObjectMapper();
    private String createUrl(String baseUrl, String endpointUri, String tickerSymbol) {
        String fullUrl = baseUrl + endpointUri;
        return String.format(fullUrl, tickerSymbol, apiKey);
    }

    private String createPricesUrl(String fromDate, String toDate, String tickerSymbol) {
        String fullUrl = baseUrl + pricesUri;
        return String.format(fullUrl, tickerSymbol, fromDate, toDate, apiKey);
    }

    public String createProfileUrl(String tickerSymbol) {
        return this.createUrl(this.baseUrl, this.profileUri, tickerSymbol);
    }

    public String createRatioUrl(String tickerSymbol) {
      return this.createUrl(this.baseUrl, this.ratiosUri, tickerSymbol);
    }

  private PriceBean createPriceBean(String priceDate, Map<String,Object> priceData) {
    StringBuilder builder = new StringBuilder();
    builder.append(priceDate);
    builder.append(String.format(",%s", priceData.get("open")));
    builder.append(String.format(",%s", priceData.get("high")));
    builder.append(String.format(",%s", priceData.get("low")));
    builder.append(String.format(",%s", priceData.get("close")));
    builder.append(String.format(",%s", priceData.get("volume")));
    return new PriceBean(builder.toString());
  }

  private List<PriceBean> transformPrices(String jsonString) {
      List<PriceBean> prices = new ArrayList<>();
      Map<String,Object> priceMap = null;
      try {
        priceMap = this.mapper.readValue(jsonString, Map.class);
        List<Map<String,Object>> priceItems = (List<Map<String,Object>>)priceMap.get("historical");
        if (priceItems != null) {
          for (Map<String,Object> price: priceItems) {
            prices.add(this.createPriceBean((String)price.get("date"), price));
          }
        }
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
      return prices;
    }

    @Override
    public List<PriceBean> downloadPrices(String tickerSymbol, int days) {
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(days);
        String toDateParam = toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fromDateParam = fromDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        URI uri = URI.create(this.createPricesUrl(fromDateParam, toDateParam, tickerSymbol));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        List<PriceBean> prices = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            prices = this.transformPrices(response.body());
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        return prices;
    }

    @Override
    public Map<String, Object> retrieveCompanyInfo(String tickerSymbol) {
        List<Map<String,Object>> result = null;
        Map<String,Object> returnObj = null;
        URI uri = URI.create(this.createProfileUrl(tickerSymbol));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                try {
                    result = this.mapper.readValue(response.body(), List.class);
                    if (!result.isEmpty()) {
                        returnObj = result.get(0);
                    }
                    else {
                        returnObj = this.mapper.readValue("{}", Map.class);
                    }
                } catch (JsonProcessingException e) {
                    Map<String,Object> errorMsg = new HashMap<>();
                    errorMsg.put("message", "Failed to parse JSON string");
                    errorMsg.put("errorCode", String.valueOf(response.statusCode()));
                    returnObj = errorMsg;
                }
            }
            else {
                Map<String,Object> errorMsg = new HashMap<>();
                errorMsg.put("message", response.body());
                errorMsg.put("errorCode", String.valueOf(response.statusCode()));
                returnObj = errorMsg;
            }
        } catch (IOException | InterruptedException e) {
            Map<String,Object> errorMsg = new HashMap<>();
            errorMsg.put("message", e.getMessage());
            errorMsg.put("errorCode", String.valueOf(response.statusCode()));
            returnObj = errorMsg;
        }
        return returnObj;
    }

  private List<FinancialRatio> transformRatios(List<Map<String,Object>> ratioResults) {
      List<FinancialRatio> ratioList = new ArrayList<>();
      FinancialRatio ratio = null;
      for (Map<String,Object> item: ratioResults) {
        ratio = mapper.convertValue(item, FinancialRatio.class);
        ratio.setFinRatioId(String.format("%s:%s", ratio.getSymbol(), ratio.getDate()));
        ratio.setCreateDate(LocalDateTime.now(ZoneId.of("UTC")));
        ratioList.add(ratio);
      }
      return ratioList;
  }

  @Override
  public List<FinancialRatio> retrieveFinancialRatios(String tickerSymbol) {
    URI uri = URI.create(this.createRatioUrl(tickerSymbol));
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
    HttpClient client = HttpClient.newHttpClient();
    List<Map<String,Object>> ratioList = null;
    List<FinancialRatio> ratios = new ArrayList<>();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        ratioList = mapper.readValue(response.body(), List.class);
        ratios = this.transformRatios(ratioList);
      }
    } catch (IOException | InterruptedException e) {
      LOGGER.error(e.getMessage());
    }
    return ratios.stream()
        .sorted((o1,o2)->{return (o1.getDate().compareTo(o2.getDate())*-1);})
        .limit(5)
        .toList();
  }
}
