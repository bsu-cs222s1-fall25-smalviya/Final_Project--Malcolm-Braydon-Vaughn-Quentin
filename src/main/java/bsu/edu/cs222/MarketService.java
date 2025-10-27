package bsu.edu.cs222;

import java.io.IOException;
import java.util.Objects;

/** Thin service: enforces "must have an Account", and returns raw JSON strings. */
public final class MarketService {
    private final MarketApi api;

    public MarketService(MarketApi api) { this.api = Objects.requireNonNull(api); }

    private static void requireAccount(Account account) {
        if (account == null) throw new IllegalStateException("No account loaded.");
    }

    public String quoteFor(Account account, String symbol) throws IOException, InterruptedException {
        requireAccount(account); return api.getRawQuote(symbol);
    }
    public String gainersFor(Account account, int limit) throws IOException, InterruptedException {
        requireAccount(account); return api.getRawTopGainers(limit);
    }
    public String screenerFor(Account account, ExchangeVariant ex, int limit) throws IOException, InterruptedException {
        requireAccount(account); return api.getRawScreenerByExchange(ex, limit);
    }
    public String searchFor(Account account, String query, int limit) throws IOException, InterruptedException {
        requireAccount(account); return api.searchRaw(query, limit);
    }
}
