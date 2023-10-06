package org.rhm.stock.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
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
        Map<String,Object> result = null;
        URI uri = URI.create(this.createProfileUrl("SMCI"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        return result;
    }
}
