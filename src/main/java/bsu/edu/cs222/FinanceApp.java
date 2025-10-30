package bsu.edu.cs222;

import java.io.Console;
import java.util.Scanner;

/** Demo main that uses your existing Account + the API service. */
public final class FinanceApp {
    public static void main(String[] args) throws Exception {
        Account.loadAccounts();

        if (noInteractiveConsole()) {
            runNonInteractiveDemo();
            return;
        }

        // ----- interactive mode -----
        Scanner in = new Scanner(System.in);
        System.out.print("1) Login or 2) Create account? ");
        int choice = Integer.parseInt(in.nextLine());
        System.out.print("Username: ");
        String user = in.nextLine();
        System.out.print("Password: ");
        String pass = in.nextLine();

        Account account = (choice == 1)
                ? Account.loginExisting(user, pass)
                : Account.createAccount(user, pass);

        if (account == null) {
            System.out.println("Could not load/create account. Exiting.");
            return;
        }

        runApiFlow(account);
    }

    private static boolean noInteractiveConsole() {
        return System.console() == null;
    }

    private static void runNonInteractiveDemo() throws Exception {
        // Use a deterministic demo account so CI / non-interactive runs still work
        String user = "demo";
        String pass = "demo";
        Account account = Account.loginExisting(user, pass);
        if (account == null) {
            account = Account.createAccount(user, pass);
        }
        System.out.println("(non-interactive) Logged in as: " + account.getUserName());
        runApiFlow(account);
    }

    private static void runApiFlow(Account account) throws Exception {
        MarketApi api = ApiFactory.create(); // live w/ fallback
        MarketService svc = new MarketService(api);

        String rawAapl = svc.quoteFor(account, "AAPL");
        var quotesAapl = MarketParser.parseQuotes(rawAapl);
        System.out.println("\nAAPL quote:");
        System.out.println(MarketFormatter.formatQuotes(quotesAapl));

        String rawGainers = svc.gainersFor(account, 3);
        var quotesGainers = MarketParser.parseQuotes(rawGainers);
        System.out.println("\nTop gainers:");
        System.out.println(MarketFormatter.formatQuotes(quotesGainers));

        String rawScreener = svc.screenerFor(account, ExchangeVariant.NASDAQ, 5);
        var quotesScreener = MarketParser.parseQuotes(rawScreener);
        System.out.println("\nScreener (NASDAQ):");
        System.out.println(MarketFormatter.formatQuotes(quotesScreener));

        String rawSearch = svc.searchFor(account, "Microsoft", 2);
        var quotesSearch = MarketParser.parseQuotes(rawSearch);
        System.out.println("\nSearch 'Microsoft':");
        System.out.println(MarketFormatter.formatQuotes(quotesSearch));

        try { Account.saveAccounts(); } catch (Throwable ignored) {}
    }

}
