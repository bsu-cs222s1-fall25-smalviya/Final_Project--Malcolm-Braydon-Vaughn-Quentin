package bsu.edu.cs222;

public class MarketFormatter {

    /**
     * Format a quote like:
     * AAPL: 189.23 (+1.23, +0.65%) as of 2025-01-01
     */
    public String format(StockQuote quote) {
        double price = quote.getPrice().doubleValue();
        double change = quote.getChangeAmount().doubleValue();
        double percent = quote.getChangePercent().doubleValue();

        String prefix = change >= 0 ? "+" : "";
        String symbol = quote.getSymbol();

        return String.format(
                "%s: %.2f (%s%.2f, %s%.2f%%) as of %s",
                symbol,
                price,
                prefix,
                change,
                prefix,
                percent,
                quote.getLastTradingDay()
        );
    }
}
