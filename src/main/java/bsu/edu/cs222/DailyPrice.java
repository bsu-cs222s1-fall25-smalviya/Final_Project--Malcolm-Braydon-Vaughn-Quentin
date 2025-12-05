package bsu.edu.cs222;

import java.math.BigDecimal;

public class DailyPrice {

    private final String date;
    private final BigDecimal close;

    public DailyPrice(String date, BigDecimal close) {
        this.date = date;
        this.close = close;
    }

    public String getDate() {
        return date;
    }

    public BigDecimal getClose() {
        return close;
    }
}
