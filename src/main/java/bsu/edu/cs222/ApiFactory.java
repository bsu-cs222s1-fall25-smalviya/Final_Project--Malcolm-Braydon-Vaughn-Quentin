package bsu.edu.cs222;

public final class ApiFactory {
    private ApiFactory() {}

    public static MarketApi createQuoteOnly() {
        String key = ApiConfig.apiKeyOrNull();


        boolean allowDemo = Boolean.getBoolean("fmp.allowDemo")
                || "true".equalsIgnoreCase(System.getenv("FMP_ALLOW_DEMO"));
        boolean demoOnly  = Boolean.getBoolean("fmp.demoOnly")
                || "true".equalsIgnoreCase(System.getenv("FMP_DEMO_ONLY"));

        Endpoint ep = "short".equalsIgnoreCase(System.getProperty("fmp.quote"))
                ? Endpoint.QUOTE_SHORT : Endpoint.QUOTE;

        if (demoOnly) {
            ApiDiagnostics.setLiveMode("DEMO");
            System.out.println("[API] DEMO ONLY (" + ep + ")");
            return new ResilientMarketApi(
                    new HttpMarketApi(ApiConfig.BASE_URL, "demo", ep),
                    new FmpMarketApiStub()
            );
        }

        if (key == null || key.isBlank()) {
            ApiDiagnostics.setStubMode();
            System.out.println("[API] STUB (no key)");
            return new FmpMarketApiStub();
        }

        ApiDiagnostics.setLiveMode(ApiConfig.mask(key));
        System.out.println("[API] LIVE (" + ep + ") with fallback (key=" + ApiConfig.mask(key) + ")"
                + (allowDemo ? " + DEMO-on-401/403" : ""));

        MarketApi liveChain = allowDemo
                ? new LiveWithDemoFallbackMarketApi(
                new HttpMarketApi(ApiConfig.BASE_URL, key, ep),
                new HttpMarketApi(ApiConfig.BASE_URL, "demo", ep))
                : new HttpMarketApi(ApiConfig.BASE_URL, key, ep);

        return new ResilientMarketApi(liveChain, new FmpMarketApiStub());

        return new HttpMarketApi("https://financialmodelingprep.com", key, Endpoint.QUOTE_SHORT);
    }
    }
}
