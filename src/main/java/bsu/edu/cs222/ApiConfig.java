package bsu.edu.cs222;

//Holds configuration for the Alpha Vantage API.
// Builds the correct URLs for quote and daily series endpoints.

public class ApiConfig {

    // Used if user and env var are both missing
    private static final String FALLBACK_KEY = "demo";

    private final String apiKey;
    private final Endpoint endpoint;

    public ApiConfig(String apiKey, Endpoint endpoint) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key is required");
        }
        this.apiKey = apiKey;
        this.endpoint = endpoint;
    }


     //Main entry point: build config from a key the user typed.
    //If userKey is null/blank, we fall back to environment/demo.

    public static ApiConfig fromUserInput(String userKey) {
        if (userKey != null && !userKey.isBlank()) {
            return new ApiConfig(userKey.trim(), Endpoint.ALPHA_VANTAGE);
        }
        return fromEnv();
    }


     //Build config from ALPHAVANTAGE_API_KEY environment variable.
     //If that is missing, use the demo key.

    public static ApiConfig fromEnv() {
        String key = System.getenv("ALPHAVANTAGE_API_KEY");
        if (key == null || key.isBlank()) {
            System.out.println("[API] ALPHAVANTAGE_API_KEY not set, using demo key.");
            key = FALLBACK_KEY;
        }
        return new ApiConfig(key, Endpoint.ALPHA_VANTAGE);
    }

    public String getApiKey() {
        return apiKey;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }


     //Build the GLOBAL_QUOTE URL for a symbol.

    public String buildQuoteUrl(String symbol) {
        return endpoint.getBaseUrl()
                + "?function=GLOBAL_QUOTE"
                + "&symbol=" + symbol
                + "&apikey=" + apiKey;
    }


     //Build the TIME_SERIES_DAILY (compact) URL for a symbol.

    public String buildDailySeriesUrl(String symbol) {
        return endpoint.getBaseUrl()
                + "?function=TIME_SERIES_DAILY"
                + "&symbol=" + symbol
                + "&outputsize=compact"
                + "&apikey=" + apiKey;
    }
}
