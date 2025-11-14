package bsu.edu.cs222;

public final class FmpMarketApiStub implements MarketApi {
    @Override
    public String getRawQuote(String symbolsCsv) {
        String[] syms = symbolsCsv.toUpperCase().split(",");
        StringBuilder sb = new StringBuilder("[");
        for (int i=0;i<syms.length;i++) {
            if (i>0) sb.append(",");
            sb.append("{\"symbol\":\"").append(syms[i]).append("\",")
                    .append("\"name\":\"Stub Company Inc.\",")
                    .append("\"price\":123.45,")
                    .append("\"change\":1.23,")
                    .append("\"changesPercentage\":\"1.01%\",")
                    .append("\"volume\":8508839}");
        }
        return sb.append("]").toString();
    }
}
