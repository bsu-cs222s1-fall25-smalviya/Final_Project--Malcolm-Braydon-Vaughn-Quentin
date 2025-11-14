package bsu.edu.cs222;

import java.io.IOException;

public final class LiveWithDemoFallbackMarketApi implements MarketApi {
    private final HttpMarketApi live;
    private final HttpMarketApi demo; // may be null

    public LiveWithDemoFallbackMarketApi(HttpMarketApi live, HttpMarketApi demo) {
        this.live = live;
        this.demo = demo;
    }

    @Override
    public String getRawQuote(String symbolsCsv) throws Exception {
        try {
            return live.getRawQuote(symbolsCsv);
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if ((msg.contains("HTTP 401") || msg.contains("HTTP 403")) && demo != null) {
                System.out.println("[API] 401/403 from primary key, retrying with DEMO...");
                return demo.getRawQuote(symbolsCsv);
            }
            throw e;
        }
    }
}
