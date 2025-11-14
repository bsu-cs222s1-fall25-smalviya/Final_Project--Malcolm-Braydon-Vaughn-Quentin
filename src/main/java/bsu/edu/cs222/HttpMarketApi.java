package bsu.edu.cs222;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class HttpMarketApi implements MarketApi {
    private final String base;
    private final String key;
    private final Endpoint endpoint;
    private final HttpClient http = HttpClient.newHttpClient();

    public HttpMarketApi(String base, String key, Endpoint ep) {
        this.base = base;
        this.key = key;
        this.endpoint = ep;
    }

    @Override
    public String getRawQuote(String symbol) throws IOException, InterruptedException {
        // Build STABLE endpoint URL (no /api/v3/)
        String url = "https://financialmodelingprep.com"
                + "/stable/quote-short/"
                + symbol.toUpperCase()
                + "?apikey=" + key;   // key must be your real key

        System.out.println("[HTTP] GET " + url); // debug: see exactly what we call

        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() < 200 || res.statusCode() >= 300) {
            System.out.println("[FMP ERROR] code=" + res.statusCode() + " body=" + res.body());
            throw new IOException("HTTP " + res.statusCode() + " for " + url);
        }
        return res.body();
    }

}
