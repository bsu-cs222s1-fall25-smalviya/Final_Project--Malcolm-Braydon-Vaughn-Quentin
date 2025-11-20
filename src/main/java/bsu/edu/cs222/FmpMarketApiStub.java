package bsu.edu.cs222;

/**
 * Simple stub that returns a hard coded Alpha Vantage style GLOBAL_QUOTE JSON.
 * Used as a fallback when the live HTTP call fails.
 */
public class FmpMarketApiStub implements MarketApi {

    @Override
    public String getQuote(String symbol) {
        String upper = symbol.toUpperCase();
        return "{\n" +
                "  \"Global Quote\": {\n" +
                "    \"01. symbol\": \"" + upper + "\",\n" +
                "    \"05. price\": \"123.45\",\n" +
                "    \"09. change\": \"1.23\",\n" +
                "    \"10. change percent\": \"1.01%\",\n" +
                "    \"07. latest trading day\": \"2025-01-01\"\n" +
                "  }\n" +
                "}";
    }
}
