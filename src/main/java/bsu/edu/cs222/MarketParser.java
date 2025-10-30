package bsu.edu.cs222;

import bsu.edu.cs222.model.StockQuote;
import java.util.ArrayList;
import java.util.List;

public final class MarketParser {
    private MarketParser() {}

    public static List<StockQuote> parseQuotes(String rawJson) {
        List<StockQuote> result = new ArrayList<>();
        if (rawJson == null || rawJson.isBlank()) return result;

        String trimmed = rawJson.trim();
        if (trimmed.startsWith("[")) trimmed = trimmed.substring(1);
        if (trimmed.endsWith("]")) trimmed = trimmed.substring(0, trimmed.length() - 1);

        String[] objects = trimmed.split("\\},\\s*\\{");

        for (String obj : objects) {
            String json = obj.replace("{", "").replace("}", "");

            String symbol = extract(json, "\"symbol\":\"", "\"");
            String name = extract(json, "\"name\":\"", "\"");
            double price = parseDouble(extract(json, "\"price\":", ","));
            double change = parseDouble(extract(json, "\"change\":", ","));

            // Try as a string with quotes first, then as a number
            String pctStr = extract(json, "\"changesPercentage\":\"", "\"");
            double percent = pctStr.isEmpty()
                    ? parseDouble(extract(json, "\"changesPercentage\":", ",")) // numeric form
                    : parsePercent(pctStr);

            result.add(new StockQuote(symbol, name, price, change, percent));
        }
        return result;
    }

    private static String extract(String text, String start, String end) {
        int i = text.indexOf(start);
        if (i == -1) return "";
        int j = text.indexOf(end, i + start.length());
        if (j == -1) j = text.length();
        return text.substring(i + start.length(), j);
    }

    private static double parseDouble(String val) {
        try {
            if (val == null) return 0.0;
            val = val.replace("\"", "").replace("%", "").replace("+", "").replace(",", "").trim();
            if (val.isEmpty()) return 0.0;
            return Double.parseDouble(val);
        } catch (Exception e) { return 0.0; }
    }

    private static double parsePercent(String val) {
        try {
            if (val == null) return 0.0;
            val = val.replace("%", "").replace("+", "").replace("\"", "").trim();
            if (val.isEmpty()) return 0.0;
            return Double.parseDouble(val);
        } catch (Exception e) { return 0.0; }
    }
}
