package bsu.edu.cs222;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Portfolio implements Serializable {
    private static final long serialVersionUID = 1L;

    // Example: store symbol -> number of shares
    private Map<String, Integer> holdings = new HashMap<>();

    public void addStock(String symbol, int shares) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + shares);
    }

    public void removeStock(String symbol, int shares) {
        if (holdings.containsKey(symbol)) {
            int newCount = holdings.get(symbol) - shares;
            if (newCount <= 0) holdings.remove(symbol);
            else holdings.put(symbol, newCount);
        }
    }

    public Map<String, Integer> getHoldings() {
        return holdings;
    }

    @Override
    public String toString() {
        if (holdings.isEmpty()) return "Portfolio is empty.";
        StringBuilder sb = new StringBuilder("Portfolio:\n");
        for (var entry : holdings.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" shares\n");
        }
        return sb.toString();
    }
}

