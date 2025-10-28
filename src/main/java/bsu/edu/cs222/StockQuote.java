package bsu.edu.cs222.model;

public record StockQuote(
        String symbol,
        String name,
        double price,
        double change,
        double changesPercentage
) {}

