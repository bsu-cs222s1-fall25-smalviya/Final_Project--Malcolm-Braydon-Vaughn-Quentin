package bsu.edu.cs222;

import java.io.IOException;
import java.util.Objects;

/** Stub responses (no network). Used automatically if no API key is set. */
public final class FmpMarketApiStub implements MarketApi {
    @Override
    public String getRawQuote(String symbol) throws IOException {
        Objects.requireNonNull(symbol, "symbol");
        return """
               [
                 {"symbol":"%s","name":"Stub Company Inc.","price":123.45,"change":1.23,"changesPercentage":1.01,"volume":4567890}
               ]
               """.formatted(symbol.toUpperCase());
    }

    @Override
    public String getRawTopGainers(int limit) throws IOException {
        return """
               [
                 {"ticker":"AAA","companyName":"AAA Corp","price":10.0,"changesPercentage":5.0,"change":0.5,"volume":100000},
                 {"ticker":"BBB","companyName":"BBB Ltd","price":20.0,"changesPercentage":4.1,"change":0.8,"volume":200000},
                 {"ticker":"CCC","companyName":"CCC PLC","price":30.0,"changesPercentage":3.2,"change":0.9,"volume":300000}
               ]
               """;
    }

    @Override
    public String getRawScreenerByExchange(ExchangeVariant exchange, int limit) throws IOException {
        Objects.requireNonNull(exchange, "exchange");
        return """
               [
                 {"symbol":"%s1","companyName":"%s One","exchangeShortName":"%s","price":11.11,"marketCap":1000000000},
                 {"symbol":"%s2","companyName":"%s Two","exchangeShortName":"%s","price":22.22,"marketCap":2000000000}
               ]
               """.formatted(
                exchange.name(), exchange.name(), exchange.name(),
                exchange.name(), exchange.name(), exchange.name()
        );
    }

    @Override
    public String searchRaw(String query, int limit) throws IOException {
        Objects.requireNonNull(query, "query");
        String q = query.isBlank() ? "UNKNOWN" : query;
        return """
               [
                 {"symbol":"%s","name":"%s Holdings","exchangeShortName":"NASDAQ"},
                 {"symbol":"%sX","name":"%s X","exchangeShortName":"NYSE"}
               ]
               """.formatted(q.toUpperCase(), cap(q), q.toUpperCase(), cap(q));
    }

    private static String cap(String s) {
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
