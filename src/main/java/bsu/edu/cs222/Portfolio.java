package bsu.edu.cs222;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Portfolio {

    private final String ownerUsername;
    private final Map<String, Integer> holdings = new LinkedHashMap<>();

    public Portfolio(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void addShares(String symbol, int shares) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol is required");
        }
        if (shares <= 0) {
            throw new IllegalArgumentException("Shares must be positive");
        }
        String key = symbol.toUpperCase();
        holdings.put(key, holdings.getOrDefault(key, 0) + shares);
    }

    public void removeSymbol(String symbol) {
        if (symbol == null) {
            return;
        }
        holdings.remove(symbol.toUpperCase());
    }

    public Map<String, Integer> getHoldings() {
        return Collections.unmodifiableMap(holdings);
    }

    public boolean isEmpty() {
        return holdings.isEmpty();
    }
}
