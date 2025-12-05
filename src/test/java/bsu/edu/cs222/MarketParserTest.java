package bsu.edu.cs222;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarketParserTest {

    @Test
    void parse_invalidQuoteJsonThrowsIllegalArgumentException() {
        MarketParser parser = new MarketParser();

        // Clearly invalid or incomplete JSON for a quote
        String json = "{}";

        assertThrows(IllegalArgumentException.class,
                () -> parser.parse(json),
                "Parser should reject invalid or missing quote data");
    }

    @Test
    void parseDailySeries_invalidJsonThrowsIllegalArgumentException() {
        MarketParser parser = new MarketParser();

        // Missing Time Series (Daily) completely
        String json = "{}";

        assertThrows(IllegalArgumentException.class,
                () -> parser.parseDailySeries("IBM", json, 5),
                "Parser should reject invalid or missing daily series data");
    }
}
