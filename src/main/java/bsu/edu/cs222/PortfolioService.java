package bsu.edu.cs222;

import java.io.IOException;
import java.util.Map;

public class PortfolioService {

    private final MarketService marketService;

    public PortfolioService(MarketService marketService) {
        this.marketService = marketService;
    }


     //Build a human-readable summary of the portfolio,
     //including per-position value and approximate daily gain/loss.

    public String formatPortfolio(Portfolio portfolio) {
        if (portfolio.isEmpty()) {
            return "Your portfolio is empty.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Portfolio for ")
                .append(portfolio.getOwnerUsername())
                .append(":\n");

        double totalValue = 0.0;
        double totalChange = 0.0;

        for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
            String symbol = entry.getKey();
            int shares = entry.getValue();
            try {
                StockQuote quote = marketService.fetchQuote(symbol);
                double price = quote.getPrice().doubleValue();
                double value = price * shares;
                double perShareChange = quote.getChangeAmount().doubleValue();
                double changeDollars = perShareChange * shares;

                totalValue += value;
                totalChange += changeDollars;

                String prefix = changeDollars >= 0 ? "+" : "";
                sb.append(String.format(
                        "  %s: %d shares @ %.2f (%.2f total, %s%.2f today)%n",
                        symbol, shares, price, value, prefix, changeDollars
                ));
            } catch (IOException e) {
                sb.append(String.format(
                        "  %s: error fetching quote: %s%n",
                        symbol, e.getMessage()
                ));
            }
        }

        if (totalValue > 0.0) {
            double previousValue = totalValue - totalChange;
            double percent = previousValue == 0.0
                    ? 0.0
                    : (totalChange / previousValue) * 100.0;
            String prefix = totalChange >= 0 ? "+" : "";
            sb.append(String.format(
                    "Approximate portfolio value: %.2f (%s%.2f today, %s%.2f%%)%n",
                    totalValue, prefix, totalChange, prefix, percent
            ));
        } else {
            sb.append("Approximate portfolio value: 0.00\n");
        }

        return sb.toString();
    }

    /**
     * Console-friendly version used by FinanceApp.
     */
    public void printPortfolio(Portfolio portfolio) {
        System.out.print(formatPortfolio(portfolio));
    }
}
