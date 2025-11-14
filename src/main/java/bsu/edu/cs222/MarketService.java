package bsu.edu.cs222;

import bsu.edu.cs222.model.StockQuote;
import java.util.*;

public final class MarketService {
    private final MarketApi api;
    public MarketService(MarketApi api){ this.api = api; }

    public String quoteFor(Account a, String symbol) throws Exception {
        ensure(a);
        return api.getRawQuote(symbol);
    }

    public String quotesForMany(Account a, List<String> symbols) throws Exception {
        ensure(a);
        if (symbols.isEmpty()) return "[]";
        String joined = String.join(",", new ArrayList<>(symbols)).toUpperCase();
        return api.getRawQuote(joined);
    }

    public List<StockQuote> parseQuotes(String raw){ return MarketParser.parseQuotes(raw); }

    private void ensure(Account a){ if (a == null) throw new IllegalStateException("Not logged in"); }
}
