package org.rhm.stock.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CompanyInfoDownload {
    @Value(value = "${company.download.apikey}")
    private String apiKey;
    @Value(value = "${company.download.baseurl}")
    private String baseUrl;
    @Value(value = "${company.download.profile}")
    private String profileUri;
    @Value(value = "${company.download.ratios}")
    private String ratiosUri;
    @Value(value = "${company.download.prices}")
    private String pricesUri;
    private ObjectMapper mapper = new ObjectMapper();
    private String createUrl(String baseUrl, String endpointUri, String tickerSymbol) {
        String fullUrl = baseUrl + endpointUri;
        return String.format(fullUrl, tickerSymbol, apiKey);
    }

    private String createPricesUrl(String fromDate, String toDate, String tickerSymbol) {
        String fullUrl = baseUrl + pricesUri;
        return String.format(fullUrl, tickerSymbol, fromDate, toDate, apiKey);
    }

    public String createProfileUrl(String tickerSymbol) {
        String url = this.createUrl(this.baseUrl, this.profileUri, tickerSymbol);
        return url;
    }

    public Map<String, Object> retrieveProfile(String tickerSymbol) {
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
                    returnObj = result.get(0);
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
}
