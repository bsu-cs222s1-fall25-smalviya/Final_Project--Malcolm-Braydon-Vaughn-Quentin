package bsu.edu.cs222;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioServiceTest {

    @Test
    void formatPortfolio_showsMessageForEmptyPortfolio() {
        // Use a dummy MarketApi so no HTTP is called
        MarketApi dummyApi = new MarketApi() {
            @Override
            public String getQuote(String symbol) {
                return "{}";
            }

            @Override
            public String getDailySeries(String symbol) {
                return "{}";
            }
        };

        MarketParser parser = new MarketParser();
        MarketFormatter formatter = new MarketFormatter();
        MarketService marketService = new MarketService(dummyApi, parser, formatter);
        PortfolioService portfolioService = new PortfolioService(marketService);

        Portfolio portfolio = new Portfolio("test-user");

        String text = portfolioService.formatPortfolio(portfolio);

        assertNotNull(text);
        assertTrue(text.toLowerCase().contains("empty"),
                "Portfolio output for no holdings should mention that it is empty");
    }
}
