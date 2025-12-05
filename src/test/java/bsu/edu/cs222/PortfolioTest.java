package bsu.edu.cs222;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

    @Test
    void addShares_forNewSymbolCreatesEntry() {
        Portfolio portfolio = new Portfolio("test-user");

        portfolio.addShares("AAPL", 5);

        Map<String, Integer> holdings = portfolio.getHoldings();
        assertEquals(5, holdings.get("AAPL"));
    }

    @Test
    void addShares_forExistingSymbolAccumulatesShares() {
        Portfolio portfolio = new Portfolio("test-user");

        portfolio.addShares("MSFT", 3);
        portfolio.addShares("MSFT", 7);

        Map<String, Integer> holdings = portfolio.getHoldings();
        assertEquals(10, holdings.get("MSFT"),
                "Shares for the same symbol should accumulate");
    }

    @Test
    void addShares_rejectsZeroOrNegativeShares() {
        Portfolio portfolio = new Portfolio("test-user");

        assertThrows(IllegalArgumentException.class,
                () -> portfolio.addShares("TSLA", 0),
                "Zero shares should not be allowed");

        assertThrows(IllegalArgumentException.class,
                () -> portfolio.addShares("TSLA", -5),
                "Negative shares should not be allowed");
    }

    @Test
    void getHoldings_returnsEmptyMapForNewPortfolio() {
        Portfolio portfolio = new Portfolio("new-user");

        assertTrue(portfolio.getHoldings().isEmpty(),
                "New portfolio should start with no holdings");
    }
}
