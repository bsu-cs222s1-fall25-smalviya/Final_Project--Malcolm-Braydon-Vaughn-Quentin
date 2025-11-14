package bsu.edu.cs222;

public final class ApiConfig {
    private ApiConfig() {}

    // Stable base (no /api/v3)
    public static final String BASE_URL = "https://financialmodelingprep.com";

    // Read the API key from VM option or env.
    public static String apiKeyOrNull() {
        String vm = System.getProperty("fmp.apiKey");
        if (vm != null && !vm.isBlank()) return vm.trim();
        String env = System.getenv("FMP_API_KEY");
        if (env != null && !env.isBlank()) return env.trim();
        return null;
    }


    public static String mask(String key) {
        if (key == null || key.length() < 6) return "****";
        return key.substring(0, 3) + "..." + key.substring(key.length() - 3);
    }
}
