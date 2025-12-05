package bsu.edu.cs222;

import java.io.IOException;


 //Abstraction over the market data provider.

public interface MarketApi {

    // Live quote JSON for a single symbol (GLOBAL_QUOTE). */
    String getQuote(String symbol) throws IOException;

    /** Daily time-series JSON for a single symbol (TIME_SERIES_DAILY, compact). */
    String getDailySeries(String symbol) throws IOException;
}
