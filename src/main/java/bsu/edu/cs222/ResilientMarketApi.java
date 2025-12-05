package bsu.edu.cs222;

import java.io.IOException;

public class ResilientMarketApi implements MarketApi {

    private final MarketApi delegate;
    private final int maxAttempts;

    public ResilientMarketApi(MarketApi delegate, int maxAttempts) {
        this.delegate = delegate;
        this.maxAttempts = Math.max(1, maxAttempts);
    }

    @Override
    public String getQuote(String symbol) throws IOException {
        return withRetries(symbol, true);
    }

    @Override
    public String getDailySeries(String symbol) throws IOException {
        return withRetries(symbol, false);
    }

    private String withRetries(String symbol, boolean quote) throws IOException {
        IOException last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if (quote) {
                    return delegate.getQuote(symbol);
                } else {
                    return delegate.getDailySeries(symbol);
                }
            } catch (IOException e) {
                last = e;
                System.err.println("API attempt " + attempt + " failed: " + e.getMessage());
            }
        }
        throw last;
    }
}
