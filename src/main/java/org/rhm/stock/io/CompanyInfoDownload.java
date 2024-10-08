package org.rhm.stock.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rhm.stock.domain.FinancialGrowth;
import org.rhm.stock.domain.FinancialRatio;
import org.rhm.stock.domain.KeyMetric;
import org.rhm.stock.dto.PriceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Value(value = "${company.download.key-metrics}")
    private String keyMetricsUri;
    @Value(value = "${company.download.prices}")
    private String pricesUri;
    @Value(value = "${company.download.fin-growth}")
    private String finGrowthUri;
    @Value(value = "${company.download.techind}" )
    private String techIndUri;
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

    public String createEmaUrl(String tickerSymbol, String period) {
      return this.createTechIndUrl(tickerSymbol, "1day", "ema", period);
    }

    public String createTechIndUrl(String tickerSymbol, String timeFrame, String type, String period) {
      String techIndUri = String.format(this.techIndUri, timeFrame, tickerSymbol, type, period, this.apiKey);
      return String.format("%s%s", this.baseUrl, techIndUri);
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
    List<Map<String,Object>> ratioList;
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

  @Override
  public List<KeyMetric> retrieveKeyMetrics(String tickerSymbol) {
    URI uri = URI.create(this.createUrl(this.baseUrl, this.keyMetricsUri, tickerSymbol));
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
    HttpClient client = HttpClient.newHttpClient();
    List<Map<String,Object>> keyMetricList;
    List<KeyMetric> keyMetrics = new ArrayList<>();
    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        keyMetricList = mapper.readValue(response.body(), List.class);
        keyMetricList.forEach(km->{
          KeyMetric keyMetric = mapper.convertValue(km, KeyMetric.class);
          keyMetrics.add(keyMetric);
        });
      }
    } catch (IOException | InterruptedException e) {
      LOGGER.error(e.getMessage());
    }
    return keyMetrics.stream()
        .sorted((o1,o2)->{return (o1.getDate().compareTo(o2.getDate())*-1);})
        .limit(5)
        .toList();
  }

  private List<FinancialGrowth> transformFinGrowth(List<Map<String,Object>> result) {
      List<FinancialGrowth> finGrowthList = new ArrayList<>();
      result.forEach(item ->{
        FinancialGrowth finGrowth = mapper.convertValue(item, FinancialGrowth.class);
        finGrowth.setCreateDate(LocalDateTime.now(ZoneId.of("GMT")));
        finGrowth.setFinGrowthId(String.format("%s:%s", finGrowth.getSymbol(), finGrowth.getDate()));
        finGrowthList.add(finGrowth);
      });
      return finGrowthList;
  }
  @Override
  public List<FinancialGrowth> retrieveFinancialGrowth(String tickerSymbol) {
    URI uri = URI.create(this.createUrl(this.baseUrl, this.finGrowthUri, tickerSymbol));
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
    HttpClient client = HttpClient.newHttpClient();
    List<Map<String,Object>> finGrowthResult;
    List<FinancialGrowth> financialGrowthList = new ArrayList<>();
    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        finGrowthResult = mapper.readValue(response.body(), List.class);
        financialGrowthList = this.transformFinGrowth(finGrowthResult);
      }
    } catch (IOException | InterruptedException e) {
      LOGGER.error(e.getMessage());
    }

    return financialGrowthList.stream().sorted((o1,o2)->{return o1.getDate()
        .compareTo(o2.getDate())*-1;})
        .limit(5)
        .toList();
  }

  public List<Map<String,Object>> retrieveEma(String tickerSymbol, String period) {
      URI uri = URI.create(this.createEmaUrl(tickerSymbol, period));
      HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
      HttpClient client = HttpClient.newHttpClient();
      List<Map<String,Object>> emaResult = null;
      HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        emaResult = mapper.readValue(response.body(), List.class);
      }
    } catch (IOException | InterruptedException e) {
      LOGGER.error(e.getMessage());
    }
    return emaResult;
  }

}
