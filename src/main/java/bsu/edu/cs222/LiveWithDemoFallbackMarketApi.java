package bsu.edu.cs222;

import java.io.IOException;

public class LiveWithDemoFallbackMarketApi implements MarketApi {

    private final MarketApi liveApi;
    private final MarketApi fallbackApi;

    public LiveWithDemoFallbackMarketApi(MarketApi liveApi, MarketApi fallbackApi) {
        this.liveApi = liveApi;
        this.fallbackApi = fallbackApi;
    }

    @Override
    public String getQuote(String symbol) throws IOException {
        try {
            return liveApi.getQuote(symbol);
        } catch (IOException e) {
            System.err.println("Live API failed for quote, using stub data: " + e.getMessage());
            return fallbackApi.getQuote(symbol);
        }
    }

    @Override
    public String getDailySeries(String symbol) throws IOException {
        try {
            return liveApi.getDailySeries(symbol);
        } catch (IOException e) {
            System.err.println("Live API failed for history, using stub data: " + e.getMessage());
            return fallbackApi.getDailySeries(symbol);
        }
    }
}
