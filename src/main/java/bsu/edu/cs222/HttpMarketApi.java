package bsu.edu.cs222;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/** Real HTTP calls using Java's built-in HttpClient (no external libs). */
public final class HttpMarketApi implements MarketApi {
    private final HttpClient http = HttpClient.newHttpClient();
    private final String baseUrl;
    private final String apiKey;

    public HttpMarketApi(String baseUrl, String apiKey) {
        this.baseUrl = Objects.requireNonNull(baseUrl);
        this.apiKey  = Objects.requireNonNull(apiKey);
    }

    @Override
    public String getRawQuote(String symbol) throws IOException, InterruptedException {
        require(symbol, "symbol");
        String url = baseUrl + "/quote/" + enc(symbol) + "?apikey=" + enc(apiKey);
        return get(url);
    }

    @Override
    public String getRawTopGainers(int limit) throws IOException, InterruptedException {
        String url = baseUrl + "/stock_market/gainers?limit=" + limit + "&apikey=" + enc(apiKey);
        return get(url);
    }

    @Override
    public String getRawScreenerByExchange(ExchangeVariant exchange, int limit) throws IOException, InterruptedException {
        require(exchange, "exchange");
        String url = baseUrl + "/stock-screener?exchange=" + enc(exchange.name())
                + "&limit=" + limit + "&apikey=" + enc(apiKey);
        return get(url);
    }

    @Override
    public String searchRaw(String query, int limit) throws IOException, InterruptedException {
        require(query, "query");
        String url = baseUrl + "/search?query=" + enc(query)
                + "&limit=" + limit + "&apikey=" + enc(apiKey);
        return get(url);
    }

    private String get(String url) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() / 100 != 2) throw new IOException("HTTP " + resp.statusCode() + " for " + url);
        return resp.body();
    }

    private static void require(Object v, String name) {
        if (v == null || (v instanceof String s && s.isBlank()))
            throw new IllegalArgumentException(name + " is blank");
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
