package bsu.edu.cs222;

public final class ApiFactory {
    private ApiFactory() {}

    public static MarketApi create() {
        String key = ApiConfig.apiKeyOrNull();
        if (key == null) return new FmpMarketApiStub(); // no key → stub
        // with the key try it live, but auto-fallback to stub on 401/403/etc
        return new ResilientMarketApi(new HttpMarketApi(ApiConfig.BASE_URL, key), new FmpMarketApiStub());
    }
}
