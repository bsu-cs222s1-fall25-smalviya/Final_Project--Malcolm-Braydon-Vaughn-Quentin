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
        IOException last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return delegate.getQuote(symbol);
            } catch (IOException e) {
                last = e;
                System.err.println("API attempt " + attempt + " failed: " + e.getMessage());
            }
        }
        throw last;
    }
}
