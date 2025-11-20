package bsu.edu.cs222;

public class ApiConfig {

    // Your Alpha Vantage key as a fallback so the app runs without extra setup.
    private static final String FALLBACK_KEY = "6ZBZI06NT3BXTW03";

    private final String apiKey;
    private final Endpoint quoteEndpoint;

    public ApiConfig(String apiKey, Endpoint quoteEndpoint) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key is required");
        }
        this.apiKey = apiKey;
        this.quoteEndpoint = quoteEndpoint;
    }

    public static ApiConfig fromEnv() {
        String key = System.getenv("ALPHAVANTAGE_API_KEY");
        if (key == null || key.isBlank()) {
            System.out.println("[API] ALPHAVANTAGE_API_KEY not set, using built in key.");
            key = FALLBACK_KEY;
        }
        return new ApiConfig(key, Endpoint.ALPHA_VANTAGE_GLOBAL_QUOTE);
    }

    public String getApiKey() {
        return apiKey;
    }

    public Endpoint getQuoteEndpoint() {
        return quoteEndpoint;
    }

    /**
     * Build the full quote URL for a given symbol using Alpha Vantage GLOBAL_QUOTE.
     */
    public String buildQuoteUrl(String symbol) {
        String base = quoteEndpoint.getBaseUrl();
        return base
                + "?function=GLOBAL_QUOTE"
                + "&symbol=" + symbol
                + "&apikey=" + apiKey;
    }
}
