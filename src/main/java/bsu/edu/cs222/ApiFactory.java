package bsu.edu.cs222;

public final class ApiFactory {

    private ApiFactory() { }

    public static MarketApi createMarketApi(String apiKey) {
        ApiConfig config = ApiConfig.fromUserInput(apiKey);
        ApiDiagnostics diagnostics = new ApiDiagnostics();

        MarketApi http = new HttpMarketApi(config, diagnostics);
        MarketApi resilient = new ResilientMarketApi(http, 2);
        MarketApi stub = new FmpMarketApiStub();

        return new LiveWithDemoFallbackMarketApi(resilient, stub);
    }

    public static MarketApi createMarketApi() {
        return createMarketApi(null);
    }
}
