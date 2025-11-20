package bsu.edu.cs222;

public class ApiDiagnostics {

    public void logRequest(String url) {
        System.out.println("[API] Request -> " + url);
    }

    public void logResponse(String url, int statusCode) {
        System.out.println("[API] Response <- " + statusCode + " from " + url);
    }
}
