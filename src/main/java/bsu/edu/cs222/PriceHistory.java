package bsu.edu.cs222;

import java.util.List;

public class PriceHistory {

    private final String symbol;
    private final List<DailyPrice> days;

    public PriceHistory(String symbol, List<DailyPrice> days) {
        this.symbol = symbol;
        this.days = List.copyOf(days);
    }

    public String getSymbol() {
        return symbol;
    }

    public List<DailyPrice> getDays() {
        return days;
    }
}

