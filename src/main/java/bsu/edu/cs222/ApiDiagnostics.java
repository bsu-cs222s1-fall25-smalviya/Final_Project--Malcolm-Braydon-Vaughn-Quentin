package bsu.edu.cs222;

import java.util.concurrent.atomic.AtomicInteger;

public final class ApiDiagnostics {
    private ApiDiagnostics(){}

    public enum Mode { LIVE, STUB }
    public enum Source { LIVE, STUB }

    private static volatile Mode mode = Mode.STUB;
    private static volatile String maskedKey = "none";
    private static final AtomicInteger fallbackCount = new AtomicInteger();

    private static volatile Source lastSource = Source.STUB;
    private static volatile String lastUrl = "";
    private static volatile int lastHttpStatus = 0;
    private static volatile String lastError = "";

    static void setLiveMode(String masked) { mode = Mode.LIVE; maskedKey = masked; }
    static void setStubMode() { mode = Mode.STUB; maskedKey = "none"; }

    static void logLiveSuccess(String url, int code) {
        lastSource = Source.LIVE;
        lastUrl = url;
        lastHttpStatus = code;
        lastError = "";
    }
    static void onFallback(Throwable e) {
        fallbackCount.incrementAndGet();
        lastSource = Source.STUB;
        lastHttpStatus = 0;
        lastError = (e == null) ? "" : e.getMessage();
    }

    public static String statusLine() {
        String head = "API mode=" + mode + (mode==Mode.LIVE? " (key="+maskedKey+")":"");
        String src = " lastSource=" + lastSource + " fallbacks=" + fallbackCount.get();
        String url = lastUrl.isBlank()? "" : " url=" + lastUrl;
        String code = lastHttpStatus==0? "" : " http=" + lastHttpStatus;
        String err = lastError.isBlank()? "" : " err=" + lastError;
        return head + " | " + src + url + code + err;
    }
}
