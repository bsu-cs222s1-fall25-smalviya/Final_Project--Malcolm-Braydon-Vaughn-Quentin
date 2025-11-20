package bsu.edu.cs222;

public enum Endpoint {
    ALPHA_VANTAGE_GLOBAL_QUOTE("https://www.alphavantage.co/query");

    private final String baseUrl;

    Endpoint(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
