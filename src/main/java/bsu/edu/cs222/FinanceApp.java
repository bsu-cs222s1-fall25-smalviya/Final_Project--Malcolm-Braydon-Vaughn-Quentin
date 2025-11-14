package bsu.edu.cs222;

import bsu.edu.cs222.model.StockQuote;

import java.util.List;
import java.util.Scanner;

public final class FinanceApp {

    public static void main(String[] args) throws Exception {
        // load accounts from disk
        Account.loadAccounts();
        System.out.println(
                "DEBUG key? vm=" + System.getProperty("fmp.apiKey") +
                        " env=" + System.getenv("FMP_API_KEY")
        );

        Scanner in = new Scanner(System.in);

        // login or create
        System.out.print("1) Login or 2) Create account? ");
        int lc = readInt(in);
        System.out.print("Username: "); String user = in.nextLine().trim();
        System.out.print("Password: "); String pass = in.nextLine().trim();

        Account account = (lc == 1)
                ? Account.loginExisting(user, pass)
                : Account.createAccount(user, pass);

        if (account == null) return;

        // build API + services (quote-only, with fallback to stub)
        MarketApi api = ApiFactory.createQuoteOnly();
        MarketService market = new MarketService(api);
        PortfolioService portfolios = new PortfolioService(market);

        // main menu
        while (true) {
            System.out.println();
            System.out.println(ApiDiagnostics.statusLine());
            System.out.println("""
                What would you like to do?
                1) Lookup symbol and (optionally) add to portfolio
                2) Portfolio: remove symbol
                3) Portfolio: view (short quotes)
                4) Exit
                """);
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1" -> lookupThenMaybeAdd(account, market, portfolios, in);
                case "2" -> {
                    System.out.print("Symbol to remove: ");
                    String sym = in.nextLine().trim().toUpperCase();
                    boolean removed = portfolios.removeSymbol(account, account.portfolio(), sym);
                    System.out.println(removed ? "Removed." : "Not found.");
                }
                case "3" -> {
                    if (account.portfolio().symbols().isEmpty()) {
                        System.out.println("Your portfolio is empty.");
                    } else {
                        System.out.println("Saved symbols: " + account.portfolio().symbols());
                        List<StockQuote> quotes = portfolios.fetchQuotes(account, account.portfolio());
                        System.out.println(MarketFormatter.shortTable(quotes));
                    }
                }
                case "4" -> {
                    Account.saveAccounts();   // persist accounts to accounts.dat
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Try again.");
            }
        }
    }




    private static void lookupThenMaybeAdd(
            Account account,
            MarketService market,
            PortfolioService portfolios,
            Scanner in
    ) throws Exception {
        System.out.print("Enter symbol (e.g., AAPL): ");
        String sym = in.nextLine().trim().toUpperCase();
        if (sym.isBlank()) { System.out.println("No symbol."); return; }

        // live /quote call (with fallback if live fails)
        String raw = market.quoteFor(account, sym);
        List<StockQuote> quotes = MarketParser.parseQuotes(raw);

        if (quotes.isEmpty()) {
            System.out.println("No data for " + sym + ".");
            return;
        }

        // show compact row like your screenshot
        System.out.println(MarketFormatter.shortTable(quotes));
        System.out.println(ApiDiagnostics.statusLine()); // shows LIVE vs STUB source

        // confirm add
        System.out.print("Add " + sym + " to your portfolio? (y/n): ");
        String ans = in.nextLine().trim().toLowerCase();
        if (ans.startsWith("y")) {
            boolean added = portfolios.addSymbol(account, account.portfolio(), sym);
            System.out.println(added ? "Added." : "Already present.");
        } else {
            System.out.println("Not added.");
        }
    }

    private static int readInt(Scanner in) {
        try { return Integer.parseInt(in.nextLine().trim()); }
        catch (Exception e) { return 0; }
    }
}
