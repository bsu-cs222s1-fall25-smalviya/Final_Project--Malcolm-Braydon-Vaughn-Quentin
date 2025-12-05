package bsu.edu.cs222;

import java.util.List;

public class MarketFormatter {


     // Format a single live quote.

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


     // Format a short history summary from TIME_SERIES_DAILY data.

    public String formatHistory(PriceHistory history) {
        List<DailyPrice> days = history.getDays();
        StringBuilder sb = new StringBuilder();
        sb.append("Recent closing prices for ").append(history.getSymbol()).append(":\n");

        for (DailyPrice day : days) {
            sb.append("  ")
                    .append(day.getDate())
                    .append(" : ")
                    .append(String.format("%.2f", day.getClose().doubleValue()))
                    .append("\n");
        }

        if (days.size() >= 2) {
            double newest = days.get(0).getClose().doubleValue();
            double oldest = days.get(days.size() - 1).getClose().doubleValue();
            double diff = newest - oldest;
            double base = oldest == 0.0 ? 0.0 : (diff / oldest) * 100.0;

            String prefix = diff >= 0 ? "+" : "";
            sb.append(String.format(
                    "Change over %d days: %s%.2f (%s%.2f%%)",
                    days.size(),
                    prefix,
                    diff,
                    prefix,
                    base
            ));
        }

        return sb.toString();
    }
}
