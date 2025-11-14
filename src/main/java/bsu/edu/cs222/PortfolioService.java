package bsu.edu.cs222;

import bsu.edu.cs222.model.Portfolio;
import bsu.edu.cs222.model.StockQuote;

import java.util.ArrayList;
import java.util.List;

public final class PortfolioService {
    private final MarketService svc;
    public PortfolioService(MarketService svc){ this.svc = svc; }

    public boolean addSymbol(Account a, Portfolio p, String symbol) { return p.add(symbol); }
    public boolean removeSymbol(Account a, Portfolio p, String symbol) { return p.remove(symbol); }

    public List<StockQuote> fetchQuotes(Account a, Portfolio p) throws Exception {
        if (p.symbols().isEmpty()) return List.of();
        String raw = svc.quotesForMany(a, new ArrayList<>(p.symbols()));
        return svc.parseQuotes(raw);
    }
}
