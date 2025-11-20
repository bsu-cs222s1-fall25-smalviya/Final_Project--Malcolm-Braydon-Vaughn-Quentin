package bsu.edu.cs222;

import java.math.BigDecimal;

public class StockQuote {

    private final String symbol;
    private final BigDecimal price;
    private final BigDecimal changeAmount;
    private final BigDecimal changePercent;
    private final String lastTradingDay;

    public StockQuote(String symbol,
                      BigDecimal price,
                      BigDecimal changeAmount,
                      BigDecimal changePercent,
                      String lastTradingDay) {
        this.symbol = symbol;
        this.price = price;
        this.changeAmount = changeAmount;
        this.changePercent = changePercent;
        this.lastTradingDay = lastTradingDay;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public String getLastTradingDay() {
        return lastTradingDay;
    }
}
