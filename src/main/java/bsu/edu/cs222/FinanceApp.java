package bsu.edu.cs222;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FinanceApp {

    private static final String ACCOUNTS_FILE = "accounts.dat";

    public static void main(String[] args) {
        MarketApi api = ApiFactory.createMarketApi();
        MarketParser parser = new MarketParser();
        MarketFormatter formatter = new MarketFormatter();
        MarketService marketService = new MarketService(api, parser, formatter);
        PortfolioService portfolioService = new PortfolioService(marketService);

        FinanceApp app = new FinanceApp(marketService, portfolioService);
        app.run();
    }

    private final MarketService marketService;
    private final PortfolioService portfolioService;
    private final List<Account> accounts = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    private Account currentAccount;

    public FinanceApp(MarketService marketService, PortfolioService portfolioService) {
        this.marketService = marketService;
        this.portfolioService = portfolioService;
    }

    public void run() {
        loadAccounts();
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("=== Finance App ===");
            System.out.println("1) Login");
            System.out.println("2) Create account");
            System.out.println("3) Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleCreateAccount();
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Unknown option, try again.");
            }
        }
        saveAccounts();
        System.out.println("Bye.");
    }

    // ---------- Login / account management ----------

    private void handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        Optional<Account> accountOpt = findAccount(username);
        if (!accountOpt.isPresent()) {
            System.out.println("No account with that username.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        Account account = accountOpt.get();
        if (!account.getUser().checkPassword(password)) {
            System.out.println("Wrong password.");
            return;
        }

        currentAccount = account;
        System.out.println("Welcome back, " + currentAccount.getUser().getUsername() + "!");
        userMenu();
        currentAccount = null;
    }

    private void handleCreateAccount() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();
        if (findAccount(username).isPresent()) {
            System.out.println("That username is already taken.");
            return;
        }
        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();

        User user = new User(username, password);
        Account account = new Account(user);
        accounts.add(account);
        System.out.println("Account created. You can now log in.");
    }

    private Optional<Account> findAccount(String username) {
        return accounts.stream()
                .filter(a -> a.getUser().getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    private void loadAccounts() {
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Account account = Account.fromLine(line);
                    accounts.add(account);
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping bad account line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read " + ACCOUNTS_FILE + ": " + e.getMessage());
        }
    }

    private void saveAccounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account account : accounts) {
                writer.println(account.serialize());
            }
        } catch (IOException e) {
            System.err.println("Could not write " + ACCOUNTS_FILE + ": " + e.getMessage());
        }
    }

    // ---------- Logged-in menus ----------

    private void userMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println();
            System.out.println("=== Main Menu ===");
            System.out.println("1) Stock search and tools");
            System.out.println("2) View your portfolio");
            System.out.println("3) Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleStockWorkspace();
                    break;
                case "2":
                    handleViewPortfolio();
                    break;
                case "3":
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Unknown option, try again.");
            }
        }
    }

    /**
     * "Stock workspace" that behaves like:
     * - a search bar (enter symbol)
     * - with buttons (menu choices) for actions on that symbol.
     */
    private void handleStockWorkspace() {
        if (currentAccount == null) {
            System.out.println("You must be logged in.");
            return;
        }

        String currentSymbol = null;
        boolean inWorkspace = true;

        while (inWorkspace) {
            if (currentSymbol == null) {
                System.out.print("Enter a stock symbol to work with (or press Enter to go back): ");
                String input = scanner.nextLine().trim().toUpperCase();
                if (input.isEmpty()) {
                    return; // back to main menu
                }
                currentSymbol = input;
            }

            System.out.println();
            System.out.println("=== Stock tools for " + currentSymbol + " ===");
            System.out.println("1) Get live quote");
            System.out.println("2) View 5-day price history");
            System.out.println("3) Add shares to your portfolio");
            System.out.println("4) Change symbol (new search)");
            System.out.println("5) Back to main menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleQuoteLookup(currentSymbol);
                    break;
                case "2":
                    handleViewHistory(currentSymbol);
                    break;
                case "3":
                    handleAddHolding(currentSymbol);
                    break;
                case "4":
                    currentSymbol = null; // ask for a new symbol
                    break;
                case "5":
                    inWorkspace = false;
                    break;
                default:
                    System.out.println("Unknown option, try again.");
            }
        }
    }

    // ---------- Individual actions ----------

    private void handleQuoteLookup(String symbol) {
        String formatted = marketService.fetchAndFormatQuote(symbol);
        System.out.println(formatted);
    }

    private void handleViewHistory(String symbol) {
        int days = 5;
        String summary = marketService.fetchAndFormatHistory(symbol, days);
        System.out.println(summary);
    }

    private void handleAddHolding(String symbol) {
        System.out.print("How many shares of " + symbol + "? ");
        String sharesInput = scanner.nextLine().trim();
        try {
            int shares = Integer.parseInt(sharesInput);
            currentAccount.getPortfolio().addShares(symbol, shares);
            System.out.println("Added " + shares + " shares of " + symbol + " to your portfolio.");
        } catch (NumberFormatException e) {
            System.out.println("Shares must be a whole number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleViewPortfolio() {
        if (currentAccount == null) {
            System.out.println("You must be logged in.");
            return;
        }
        portfolioService.printPortfolio(currentAccount.getPortfolio());
    }
}
