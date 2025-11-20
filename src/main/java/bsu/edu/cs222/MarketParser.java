package bsu.edu.cs222;

import java.math.BigDecimal;

/**
 * Very small JSON parser for Alpha Vantage GLOBAL_QUOTE responses.
 * It does not try to be a full JSON library, it just pulls out the fields we care about.
 */
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
}
