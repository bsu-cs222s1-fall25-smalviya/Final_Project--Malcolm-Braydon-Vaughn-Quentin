package bsu.edu.cs222;

import bsu.edu.cs222.model.StockQuote;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class MarketParserTest {

    @Test
    void parseQuotes_handlesStringAndNumericPercent() {
        String rawJson = """
            [
              {"symbol":"AAPL","name":"Apple Inc.","price":150.00,"change":-1.50,"changesPercentage":"-0.99%"},
              {"symbol":"MSFT","name":"Microsoft","price":300.25,"change":2.75,"changesPercentage":0.92}
            ]
            """;

        List<StockQuote> quotes = MarketParser.parseQuotes(rawJson);

        assertEquals(2, quotes.size());

        StockQuote aapl = quotes.get(0);
        assertEquals("AAPL", aapl.symbol());
        assertEquals("Apple Inc.", aapl.name());
        assertEquals(150.00, aapl.price(), 0.0001);
        assertEquals(-1.50, aapl.change(), 0.0001);
        assertEquals(-0.99, aapl.changesPercentage(), 0.0001);

        StockQuote msft = quotes.get(1);
        assertEquals("MSFT", msft.symbol());
        assertEquals("Microsoft", msft.name());
        assertEquals(300.25, msft.price(), 0.0001);
        assertEquals(2.75, msft.change(), 0.0001);
        assertEquals(0.92, msft.changesPercentage(), 0.0001);
    }
}
