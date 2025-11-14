package bsu.edu.cs222;

public interface MarketApi {
    // /api/v3/quote/AAPL or /api/v3/quote/AAPL,MSFT
    String getRawQuote(String symbolsCsv) throws Exception;
}
