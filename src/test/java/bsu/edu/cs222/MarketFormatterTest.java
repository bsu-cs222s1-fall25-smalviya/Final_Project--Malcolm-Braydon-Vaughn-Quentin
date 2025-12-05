package bsu.edu.cs222;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MarketFormatterTest {

    @Test
    void format_includesSymbolAndPrice() {
        // Match your actual StockQuote constructor:
        // StockQuote(String symbol,
        //            BigDecimal price,
        //            BigDecimal previousClose,
        //            BigDecimal change,
        //            String changePercent)
        StockQuote quote = new StockQuote(
                "AAPL",
                new BigDecimal("150.25"),
                new BigDecimal("149.00"),
                new BigDecimal("1.25"),
                "0.84"
        );

        MarketFormatter formatter = new MarketFormatter();

        String text = formatter.format(quote);

        assertTrue(text.contains("AAPL"),
                "Formatted quote should contain the symbol");
        assertTrue(text.contains("150.25"),
                "Formatted quote should contain the price");
    }

    @Test
    void formatHistory_includesSymbolAndDates() {
        // Match your current DailyPrice constructor:
        // DailyPrice(String dateIso, BigDecimal close)
        DailyPrice d1 = new DailyPrice("2024-12-04",
                new BigDecimal("101.00"));
        DailyPrice d2 = new DailyPrice("2024-12-03",
                new BigDecimal("100.00"));

        PriceHistory history = new PriceHistory("IBM", List.of(d1, d2));

        MarketFormatter formatter = new MarketFormatter();

        String text = formatter.formatHistory(history);

        assertTrue(text.contains("IBM"),
                "History output should include the symbol");
        assertTrue(text.contains("2024-12-04"),
                "History output should include the first date");
        assertTrue(text.contains("2024-12-03"),
                "History output should include the second date");
    }
}
