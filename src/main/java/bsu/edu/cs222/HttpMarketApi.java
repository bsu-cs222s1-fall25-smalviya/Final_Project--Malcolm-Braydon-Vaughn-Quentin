package bsu.edu.cs222;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpMarketApi implements MarketApi {

    private final ApiConfig config;
    private final ApiDiagnostics diagnostics;
    private final HttpClient httpClient;

    public HttpMarketApi(ApiConfig config, ApiDiagnostics diagnostics) {
        this.config = config;
        this.diagnostics = diagnostics;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public String getQuote(String symbol) throws IOException {
        String url = config.buildQuoteUrl(symbol);
        return sendRequest(url, "\"Global Quote\"");
    }

    @Override
    public String getDailySeries(String symbol) throws IOException {
        String url = config.buildDailySeriesUrl(symbol);
        return sendRequest(url, "\"Time Series (Daily)\"");
    }

    private String sendRequest(String url, String expectedKey) throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        diagnostics.logRequest(url);

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            diagnostics.logResponse(url, response.statusCode());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP " + response.statusCode() + " from Alpha Vantage");
            }

            String body = response.body();
            if (!body.contains(expectedKey)) {
                throw new IOException("Unexpected Alpha Vantage response: " + body);
            }

            return body;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("HTTP call interrupted", e);
        }
    }
}
