package bsu.edu.cs222;

import java.io.IOException;

/**
 * Simple abstraction over the market data provider.
 * Returns raw JSON for a single symbol.
 */
public interface MarketApi {

    String getQuote(String symbol) throws IOException;
}
