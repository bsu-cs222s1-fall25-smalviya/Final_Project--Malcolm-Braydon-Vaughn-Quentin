package bsu.edu.cs222;

public class ApiFactory {

    private ApiFactory() {
        // utility class
    }

    /**
     * Build the full MarketApi stack:
     * HttpMarketApi (Alpha Vantage) wrapped in ResilientMarketApi
     * and with a stub fallback if the live calls fail.
     */
    public static MarketApi createMarketApi() {
        ApiConfig config = ApiConfig.fromEnv();
        ApiDiagnostics diagnostics = new ApiDiagnostics();

        MarketApi httpApi = new HttpMarketApi(config, diagnostics);
        MarketApi resilient = new ResilientMarketApi(httpApi, 2);
        MarketApi stub = new FmpMarketApiStub(); // now acts as a local Alpha Vantage style stub

        return new LiveWithDemoFallbackMarketApi(resilient, stub);
    }
}
