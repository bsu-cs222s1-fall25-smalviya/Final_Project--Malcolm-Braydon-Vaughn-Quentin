package bsu.edu.cs222;

import bsu.edu.cs222.model.StockQuote;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class MarketFormatterTest {

    @Test
    void formatQuotes_truncatesLongName_andFormatsNumbers() {
        String longName = "Very very long company name that exceeds thirty characters";
        // MAX_NAME_WIDTH is 30 → expected truncated to first 27 + "..."
        String expectedTruncated = longName.substring(0, 27) + "...";

        List<StockQuote> quotes = List.of(
                new StockQuote("LONG", longName, 123.4, -2.5, 1.234)
        );

        String table = MarketFormatter.formatQuotes(quotes);

        // Header present
        assertTrue(table.contains("SYMBOL"));
        assertTrue(table.contains("NAME"));
        assertTrue(table.contains("PRICE"));
        assertTrue(table.contains("CHANGE"));
        assertTrue(table.contains("% CHANGE"));

        // Truncated name appears
        assertTrue(table.contains(expectedTruncated));

        // Two decimal places on numbers
        assertTrue(table.contains("123.40")); // price
        assertTrue(table.contains("-2.50"));  // change
        assertTrue(table.contains("1.23"));   // % change
    }
}
