package bsu.edu.cs222;

import java.io.IOException;

public interface MarketApi {
    String getRawQuote(String symbol) throws IOException, InterruptedException;
    String getRawTopGainers(int limit) throws IOException, InterruptedException;
    String getRawScreenerByExchange(ExchangeVariant exchange, int limit) throws IOException, InterruptedException;
    String searchRaw(String query, int limit) throws IOException, InterruptedException;
}
