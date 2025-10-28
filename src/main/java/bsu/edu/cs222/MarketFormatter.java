package bsu.edu.cs222;

import bsu.edu.cs222.model.StockQuote;

import java.util.List;

public final class MarketFormatter {

    private MarketFormatter() {} // prevent instantiation

    private static final int MAX_NAME_WIDTH = 30; // max width for company name

    /** Formats a list of StockQuote objects into a table with truncated names and aligned numbers. */
    public static String formatQuotes(List<StockQuote> quotes) {
        if (quotes.isEmpty()) return "No quotes found.";

        int symbolWidth = Math.max("SYMBOL".length(),
                quotes.stream().mapToInt(q -> q.symbol() != null ? q.symbol().length() : 0).max().orElse(6));
        int nameWidth = MAX_NAME_WIDTH;
        int priceWidth = "PRICE".length();
        int changeWidth = "CHANGE".length();
        int percentWidth = "% CHANGE".length();

        StringBuilder sb = new StringBuilder();
        String headerFormat = String.format("%%-%ds %%-%ds %%%ds %%%ds %%%ds%n",
                symbolWidth, nameWidth, priceWidth, changeWidth, percentWidth);

        sb.append(String.format(headerFormat, "SYMBOL", "NAME", "PRICE", "CHANGE", "% CHANGE"));
        sb.append("-".repeat(symbolWidth + nameWidth + priceWidth + changeWidth + percentWidth + 4)).append("\n");

        for (StockQuote q : quotes) {
            String name = truncate(q.name(), nameWidth);

            sb.append(String.format(
                    headerFormat,
                    q.symbol() != null ? q.symbol() : "",
                    name,
                    String.format("%.2f", q.price()),
                    String.format("%.2f", q.change()),
                    String.format("%.2f", q.changesPercentage())
            ));
        }

        return sb.toString();
    }

    /** Truncates a string if it exceeds maxWidth, adding "..." at the end. */
    private static String truncate(String text, int maxWidth) {
        if (text == null) return "";
        if (text.length() <= maxWidth) return text;
        if (maxWidth <= 3) return "..."; // handle very small widths
        return text.substring(0, maxWidth - 3) + "...";
    }

    /** Formats a list of StockQuote objects as CSV. */
    public static String formatQuotesCsv(List<StockQuote> quotes) {
        if (quotes.isEmpty()) return "";

        StringBuilder sb = new StringBuilder("SYMBOL,NAME,PRICE,CHANGE,%CHANGE\n");
        for (StockQuote q : quotes) {
            sb.append(String.join(",",
                    q.symbol() != null ? q.symbol() : "",
                    q.name() != null ? q.name() : "",
                    String.format("%.2f", q.price()),
                    String.format("%.2f", q.change()),
                    String.format("%.2f", q.changesPercentage())
            )).append("\n");
        }

        return sb.toString();
    }
}


