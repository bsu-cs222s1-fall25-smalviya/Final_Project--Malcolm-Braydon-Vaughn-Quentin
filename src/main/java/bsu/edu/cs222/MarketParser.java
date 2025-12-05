package bsu.edu.cs222;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MarketParser {

    public StockQuote parse(String json) {
        String symbol = extractField(json, "01. symbol");
        String priceStr = extractField(json, "05. price");
        String changeStr = extractField(json, "09. change");
        String changePercentStr = extractField(json, "10. change percent");
        String tradingDay = extractField(json, "07. latest trading day");

        BigDecimal price = new BigDecimal(priceStr);
        BigDecimal change = new BigDecimal(changeStr);
        BigDecimal percent = new BigDecimal(
                changePercentStr.replace("%", "").trim()
        );

        return new StockQuote(symbol, price, change, percent, tradingDay);
    }

    // package-private so tests could use it
    String extractField(String json, String key) {
        String token = "\"" + key + "\"";
        int keyIndex = json.indexOf(token);
        if (keyIndex < 0) {
            throw new IllegalArgumentException("Missing key " + key);
        }

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex < 0) {
            throw new IllegalArgumentException("Missing ':' for key " + key);
        }

        int firstQuote = json.indexOf("\"", colonIndex + 1);
        int secondQuote = json.indexOf("\"", firstQuote + 1);
        if (firstQuote < 0 || secondQuote < 0) {
            throw new IllegalArgumentException("Malformed value for key " + key);
        }

        return json.substring(firstQuote + 1, secondQuote);
    }


      //Parse a TIME_SERIES_DAILY JSON payload into a PriceHistory with up to maxDays entries.

    public PriceHistory parseDailySeries(String symbol, String json, int maxDays) {
        String key = "\"Time Series (Daily)\"";
        int seriesIndex = json.indexOf(key);
        if (seriesIndex < 0) {
            throw new IllegalArgumentException("Missing Time Series (Daily) section");
        }

        int braceStart = json.indexOf("{", seriesIndex + key.length());
        if (braceStart < 0) {
            throw new IllegalArgumentException("Malformed Time Series (Daily) block");
        }

        int pos = braceStart + 1;
        List<DailyPrice> days = new ArrayList<>();

        while (days.size() < maxDays) {
            int firstQuote = json.indexOf("\"", pos);
            if (firstQuote < 0) {
                break;
            }
            int secondQuote = json.indexOf("\"", firstQuote + 1);
            if (secondQuote < 0) {
                break;
            }

            String candidate = json.substring(firstQuote + 1, secondQuote);

            // Keys that look like dates: YYYY-MM-DD
            if (!candidate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                pos = secondQuote + 1;
                continue;
            }

            String date = candidate;

            int closeKeyIndex = json.indexOf("\"4. close\"", secondQuote);
            if (closeKeyIndex < 0) {
                break;
            }
            int colonIndex = json.indexOf(":", closeKeyIndex);
            if (colonIndex < 0) {
                break;
            }
            int valueStart = json.indexOf("\"", colonIndex + 1);
            int valueEnd = json.indexOf("\"", valueStart + 1);
            if (valueStart < 0 || valueEnd < 0) {
                break;
            }

            String closeStr = json.substring(valueStart + 1, valueEnd).trim();
            try {
                BigDecimal close = new BigDecimal(closeStr);
                days.add(new DailyPrice(date, close));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(
                        "Bad close price '" + closeStr + "' for date " + date, ex);
            }

            pos = valueEnd + 1;
        }

        if (days.isEmpty()) {
            throw new IllegalArgumentException("No daily prices found in response");
        }

        return new PriceHistory(symbol, days);
    }
}
