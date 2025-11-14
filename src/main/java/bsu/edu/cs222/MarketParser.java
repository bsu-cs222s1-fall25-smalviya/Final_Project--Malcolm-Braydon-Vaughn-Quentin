package bsu.edu.cs222;

import bsu.edu.cs222.model.StockQuote;
import java.util.*;

public final class MarketParser {
    private MarketParser(){}


    public static List<StockQuote> parseQuotes(String rawJson) {
        List<StockQuote> out = new ArrayList<>();
        if (rawJson == null || rawJson.isBlank()) return out;

        String s = rawJson.trim();
        if (s.startsWith("[")) s = s.substring(1);
        if (s.endsWith("]")) s = s.substring(0, s.length()-1);
        String[] objs = s.split("\\},\\s*\\{");

        for (String o : objs) {
            String j = o.replace("{","").replace("}","");
            String symbol = ex(j,"\"symbol\":\"","\"");
            String name   = ex(j,"\"name\":\"","\"");
            double price  = d(ex(j,"\"price\":",","));
            double change = d(ex(j,"\"change\":",","));
            double pct    = d(ex(j,"\"changesPercentage\":\"","\""));
            long volume   = l(ex(j,"\"volume\":",","));
            out.add(new StockQuote(symbol, name, price, change, pct, volume));
        }
        return out;
    }

    private static String ex(String t, String a, String b){
        int i=t.indexOf(a); if(i<0)return "";
        int j=t.indexOf(b,i+a.length()); if(j<0) j=t.length();
        return t.substring(i+a.length(), j);
    }
    private static double d(String v){
        try { return Double.parseDouble(v.replace("%","").replace("\"","").replace(",","").trim()); }
        catch(Exception e){ return 0.0; }
    }
    private static long l(String v){
        try { return Long.parseLong(v.replace("\"","").replace(",","").trim()); }
        catch(Exception e){ return 0L; }
    }
}
