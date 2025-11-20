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
}

