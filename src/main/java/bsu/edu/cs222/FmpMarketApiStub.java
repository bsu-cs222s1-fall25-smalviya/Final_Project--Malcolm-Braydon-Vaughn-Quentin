package bsu.edu.cs222;

import java.io.IOException;
import java.util.Objects;

public final class FmpMarketApiStub implements MarketApi {
    @Override
    public String getRawQuote(String symbol) throws IOException {
        Objects.requireNonNull(symbol, "symbol");
        return """
               [
                 {"symbol":"%s","name":"Stub Company Inc.","price":123.45,"change":1.23,"changesPercentage":"1.01%%"}
               ]
               """.formatted(symbol.toUpperCase());
    }

    @Override
    public String getRawTopGainers(int limit) throws IOException {
        return """
               [
                 {"symbol":"AAA","name":"AAA Corp","price":10.00,"change":0.50,"changesPercentage":"5.00%%"},
                 {"symbol":"BBB","name":"BBB Ltd","price":20.00,"change":0.80,"changesPercentage":"4.10%%"},
                 {"symbol":"CCC","name":"CCC PLC","price":30.00,"change":0.90,"changesPercentage":"3.20%%"}
               ]
               """;
    }

    @Override
    public String getRawScreenerByExchange(ExchangeVariant exchange, int limit) throws IOException {
        Objects.requireNonNull(exchange, "exchange");
        String ex = exchange.name();
        return """
               [
                 {"symbol":"%s1","name":"%s One","price":11.11,"change":0.11,"changesPercentage":"1.00%%"},
                 {"symbol":"%s2","name":"%s Two","price":22.22,"change":0.22,"changesPercentage":"1.00%%"}
               ]
               """.formatted(ex, ex, ex, ex);
    }

    @Override
    public String searchRaw(String query, int limit) throws IOException {
        Objects.requireNonNull(query, "query");
        String q = query.isBlank() ? "UNKNOWN" : query;
        return """
               [
                 {"symbol":"%s","name":"%s Holdings","price":100.00,"change":0.50,"changesPercentage":"0.50%%"},
                 {"symbol":"%sX","name":"%s X","price":101.00,"change":0.40,"changesPercentage":"0.40%%"}
               ]
               """.formatted(q.toUpperCase(), cap(q), q.toUpperCase(), cap(q));
    }

    private static String cap(String s) {
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
