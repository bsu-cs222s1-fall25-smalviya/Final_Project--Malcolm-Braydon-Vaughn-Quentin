package bsu.edu.cs222;

import bsu.edu.cs222.model.StockQuote;
import java.util.List;

public final class MarketFormatter {
    private MarketFormatter(){}


    public static String shortTable(List<StockQuote> qs) {
        if (qs.isEmpty()) return "No data.";
        int numW = String.valueOf(qs.size()).length();
        int symW = Math.max(6, qs.stream().mapToInt(q -> q.symbol()==null?0:q.symbol().length()).max().orElse(6));
        String fmt = "%"+numW+"s  %-"+symW+"s  %8s  %8s  %12s%n";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(fmt, "#","SYMBOL","PRICE","CHANGE","VOLUME"));
        sb.append("-".repeat(numW+symW+8+8+12+8)).append("\n");
        int i=1;
        for (StockQuote q: qs) {
            sb.append(String.format(fmt, i++,
                    q.symbol()==null? "" : q.symbol(),
                    String.format("%.2f", q.price()),
                    String.format("%.2f", q.change()),
                    String.format("%,d", q.volume())
            ));
        }
        return sb.toString();
    }
}
