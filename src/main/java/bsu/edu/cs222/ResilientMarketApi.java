package bsu.edu.cs222;

import java.io.IOException;

public final class ResilientMarketApi implements MarketApi {
    private final MarketApi primary;   // usually HttpMarketApi
    private final MarketApi fallback;  // FmpMarketApiStub

    public ResilientMarketApi(MarketApi primary, MarketApi fallback) {
        this.primary = primary;
        this.fallback = fallback;
    }

    @Override
    public String getRawQuote(String symbol) throws IOException, InterruptedException {
        try { return primary.getRawQuote(symbol); }
        catch (Exception e) { return fallback.getRawQuote(symbol); }
    }

    @Override
    public String getRawTopGainers(int limit) throws IOException, InterruptedException {
        try { return primary.getRawTopGainers(limit); }
        catch (Exception e) { return fallback.getRawTopGainers(limit); }
    }

    @Override
    public String getRawScreenerByExchange(ExchangeVariant exchange, int limit) throws IOException, InterruptedException {
        try { return primary.getRawScreenerByExchange(exchange, limit); }
        catch (Exception e) { return fallback.getRawScreenerByExchange(exchange, limit); }
    }

    @Override
    public String searchRaw(String query, int limit) throws IOException, InterruptedException {
        try { return primary.searchRaw(query, limit); }
        catch (Exception e) { return fallback.searchRaw(query, limit); }
    }
}
