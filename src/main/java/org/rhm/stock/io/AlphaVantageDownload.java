package org.rhm.stock.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rhm.stock.dto.PriceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AlphaVantageDownload implements PriceDownload {
  private static final Logger LOGGER = LoggerFactory.getLogger(AlphaVantageDownload.class);
  private static final String FORMAT_COMPACT = "compact";
  public static final String FORMAT_FULL = "full";
  @Value(value = "${price.download..url}")
  private String downloadUrl;
  @Value(value = "${price.download.apikey}")
  private String apiKey;
  private final ObjectMapper mapper = new ObjectMapper();
  private PriceBean createPriceBean(String priceDate, Map<String,Object> priceData) {
    StringBuilder builder = new StringBuilder();
    builder.append(priceDate);
    for (Object price: priceData.values()) {
      builder.append(String.format(",%s",price));
    }
    return new PriceBean(builder.toString());
  }

  private List<PriceBean> transformPrices(Map<String,Object> downloadResult) {
    List<PriceBean> prices = new ArrayList<>();
    Map<String,Object> priceMap = (Map<String,Object>)downloadResult.get("Time Series (Daily)");
    if (null == priceMap) {
      LOGGER.error("transformPrices - {}", downloadResult);
    }
    else {
      for (Map.Entry<String,Object> entry: priceMap.entrySet()) {
        prices.add(this.createPriceBean(entry.getKey(), (Map<String,Object>)entry.getValue()));
      }
    }
    return prices;
  }

  public List<PriceBean> downloadPrices(String tickerSymbol) {
    return this.downloadPrices(tickerSymbol, FORMAT_COMPACT);
  }
  public List<PriceBean> downloadPrices(String tickerSymbol, String format) {
    String endpoint = String.format(downloadUrl, tickerSymbol, format, apiKey);
    LOGGER.info("downloadPrices - ticker: {}", tickerSymbol);
    LOGGER.info("downloadPrices - endpoint {}", endpoint);
    HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(endpoint)).build();
    HttpClient client = HttpClient.newHttpClient();
    Optional<Map<String,Object>> priceResult = Optional.empty();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      Map<String,Object> results = mapper.readValue(response.body().getBytes(StandardCharsets.UTF_8), Map.class);
      if (response.statusCode() == 200) {
        priceResult = Optional.of(results);
      }
    }
    catch (IOException | InterruptedException e) {
      LOGGER.error(e.getMessage());
    }
    return priceResult.map(this::transformPrices).orElse(null);
  }
}
