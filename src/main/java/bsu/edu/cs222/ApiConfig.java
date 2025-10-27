package bsu.edu.cs222;

public final class ApiConfig {
    private ApiConfig() {}

    // Base URL for FMP; override with: -Dfmp.baseUrl=...
    public static final String BASE_URL =
            System.getProperty("fmp.baseUrl", "https://financialmodelingprep.com/api/v3");

    // API key lookup (JVM property first, then environment)
    public static String apiKeyOrNull() {
        String key = System.getProperty("fmp.apiKey");
        if (key == null || key.isBlank()) key = System.getenv("FMP_API_KEY");
        return (key == null || key.isBlank()) ? null : key;
    }
}
