package bsu.edu.cs222;

public final class ApiFactory {
    private ApiFactory() {}

    public static MarketApi create() {
        String key = ApiConfig.apiKeyOrNull();
        return (key == null) ? new FmpMarketApiStub()
                : new HttpMarketApi(ApiConfig.BASE_URL, key);
    }
}
