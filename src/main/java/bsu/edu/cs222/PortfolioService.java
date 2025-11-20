package bsu.edu.cs222;

import java.io.IOException;
import java.util.Map;

public class PortfolioService {

    private final MarketService marketService;

    public PortfolioService(MarketService marketService) {
        this.marketService = marketService;
    }

    public void printPortfolio(Portfolio portfolio) {
        if (portfolio.isEmpty()) {
            System.out.println("Your portfolio is empty.");
            return;
        }

        System.out.println("Portfolio for " + portfolio.getOwnerUsername() + ":");

        for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
            String symbol = entry.getKey();
            int shares = entry.getValue();
            try {
                StockQuote quote = marketService.fetchQuote(symbol);
                double price = quote.getPrice().doubleValue();
                double value = price * shares;
                System.out.printf("  %s: %d shares @ %.2f (approx %.2f total)%n",
                        symbol, shares, price, value);
            } catch (IOException e) {
                System.out.printf("  %s: error fetching quote: %s%n", symbol, e.getMessage());
            }
        }
    }
}
