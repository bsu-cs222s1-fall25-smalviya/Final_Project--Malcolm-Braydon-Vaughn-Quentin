package bsu.edu.cs222;

import java.io.IOException;

public class MarketService {

    private final MarketApi api;
    private final MarketParser parser;
    private final MarketFormatter formatter;

    public MarketService(MarketApi api, MarketParser parser, MarketFormatter formatter) {
        this.api = api;
        this.parser = parser;
        this.formatter = formatter;
    }

    public String fetchAndFormatQuote(String symbol) {
        try {
            String json = api.getQuote(symbol);
            StockQuote quote = parser.parse(json);
            return formatter.format(quote);
        } catch (IOException e) {
            return "Error contacting market data service: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Error parsing market data: " + e.getMessage();
        }
    }

    public StockQuote fetchQuote(String symbol) throws IOException {
        String json = api.getQuote(symbol);
        return parser.parse(json);
    }

    public String fetchAndFormatHistory(String symbol, int days) {
        try {
            String json = api.getDailySeries(symbol);
            PriceHistory history = parser.parseDailySeries(symbol, json, days);
            return formatter.formatHistory(history);
        } catch (IOException e) {
            return "Error contacting market data service: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Error parsing historical data: " + e.getMessage();
        }
    }


     //New: fetch the raw PriceHistory object (for graphs).

    public PriceHistory fetchHistory(String symbol, int days) throws IOException {
        String json = api.getDailySeries(symbol);
        return parser.parseDailySeries(symbol, json, days);
    }
}
