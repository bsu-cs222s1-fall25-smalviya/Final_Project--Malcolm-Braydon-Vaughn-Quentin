package bsu.edu.cs222;


 //Stub that returns hard coded Alpha Vantage style JSON.
 //Used as a fallback when the live HTTP call fails.

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

    @Override
    public String getDailySeries(String symbol) {
        String upper = symbol.toUpperCase();
        return "{\n" +
                "  \"Meta Data\": {\n" +
                "    \"2. Symbol\": \"" + upper + "\"\n" +
                "  },\n" +
                "  \"Time Series (Daily)\": {\n" +
                "    \"2025-01-05\": {\n" +
                "      \"1. open\": \"120.00\",\n" +
                "      \"2. high\": \"125.00\",\n" +
                "      \"3. low\": \"119.00\",\n" +
                "      \"4. close\": \"124.00\",\n" +
                "      \"5. volume\": \"1000000\"\n" +
                "    },\n" +
                "    \"2025-01-04\": {\n" +
                "      \"1. open\": \"118.00\",\n" +
                "      \"2. high\": \"121.00\",\n" +
                "      \"3. low\": \"117.00\",\n" +
                "      \"4. close\": \"119.50\",\n" +
                "      \"5. volume\": \"900000\"\n" +
                "    },\n" +
                "    \"2025-01-03\": {\n" +
                "      \"1. open\": \"115.00\",\n" +
                "      \"2. high\": \"118.00\",\n" +
                "      \"3. low\": \"114.00\",\n" +
                "      \"4. close\": \"117.25\",\n" +
                "      \"5. volume\": \"800000\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
