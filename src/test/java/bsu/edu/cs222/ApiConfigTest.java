package bsu.edu.cs222;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiConfigTest {

    @Test
    void buildQuoteUrl_includesSymbolFunctionAndKey() {
        ApiConfig config = ApiConfig.fromUserInput("TESTKEY123");

        String url = config.buildQuoteUrl("AAPL");

        assertTrue(url.contains("function=GLOBAL_QUOTE"),
                "Quote URL should request GLOBAL_QUOTE");
        assertTrue(url.contains("symbol=AAPL"),
                "Quote URL should contain the symbol parameter");
        assertTrue(url.contains("apikey=TESTKEY123"),
                "Quote URL should include the API key");
    }

    @Test
    void buildDailySeriesUrl_includesDailyFunctionAndCompact() {
        ApiConfig config = ApiConfig.fromUserInput("MYKEY");

        String url = config.buildDailySeriesUrl("MSFT");

        assertTrue(url.contains("function=TIME_SERIES_DAILY"),
                "History URL should request TIME_SERIES_DAILY");
        assertTrue(url.contains("symbol=MSFT"),
                "History URL should contain the symbol parameter");
        assertTrue(url.contains("outputsize=compact"),
                "History URL should use compact outputsize");
        assertTrue(url.contains("apikey=MYKEY"),
                "History URL should include the API key");
    }

    @Test
    void fromUserInput_throwsOnBlankKeyIfNoEnvOrFallback() {
        // This tests the constructor validation, not the fallback.
        // We expect IllegalArgumentException when key is missing.
        assertThrows(IllegalArgumentException.class, () -> {
            new ApiConfig("  ", Endpoint.ALPHA_VANTAGE);
        });
    }
}
