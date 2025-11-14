package bsu.edu.cs222;

public final class ResilientMarketApi implements MarketApi {
    private final MarketApi live, stub;
    public ResilientMarketApi(MarketApi live, MarketApi stub){ this.live=live; this.stub=stub; }

    @Override
    public String getRawQuote(String symbolsCsv) throws Exception {
        try { return live.getRawQuote(symbolsCsv); }
        catch (Throwable t) { ApiDiagnostics.onFallback(t); return stub.getRawQuote(symbolsCsv); }
    }
}
