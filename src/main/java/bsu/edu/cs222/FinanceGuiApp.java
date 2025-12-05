package bsu.edu.cs222;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FinanceGuiApp {

    private static final String ACCOUNTS_FILE = "accounts.dat";

    private MarketService marketService;
    private PortfolioService portfolioService;
    private final List<Account> accounts = new ArrayList<>();
    private Account currentAccount;

    private JFrame frame;
    private JTextField symbolField;
    private JTextArea outputArea;

    public FinanceGuiApp() {
        // Everything happens in start()
    }


      //Entry point for the GUI: ask for API key, wire backend,
     // load accounts + portfolios, force login, then show main window.

    public void start() {
        // 1. Ask for API key
        String apiKey = askForApiKey();

        // 2. Wire up backend
        MarketApi api = ApiFactory.createMarketApi(apiKey);
        MarketParser parser = new MarketParser();
        MarketFormatter formatter = new MarketFormatter();
        this.marketService = new MarketService(api, parser, formatter);
        this.portfolioService = new PortfolioService(marketService);

        // 3. Load accounts + portfolios from disk
        loadAccounts();
        // If you created PortfolioStorage.java earlier, this line ties portfolios to users
        PortfolioStorage.loadPortfolios(accounts);

        // 4. Force login / account creation
        showLoginFlow();
        if (currentAccount == null) {
            System.exit(0);
        }

        // 5. Build and show main UI
        initUi();
        updateWindowTitle();
        frame.setVisible(true);
    }

    private String askForApiKey() {
        String message = "Enter Alpha Vantage API key.\n"
                + "Leave blank to use ALPHAVANTAGE_API_KEY env var or demo key.";
        String input = JOptionPane.showInputDialog(
                null,
                message,
                "Alpha Vantage API Key",
                JOptionPane.QUESTION_MESSAGE
        );
        if (input == null) {
            System.exit(0);
        }
        return input.trim();
    }

    // ---------- Account loading / saving ----------

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

        // Also persist per-user holdings if you have PortfolioStorage.java
        PortfolioStorage.savePortfolios(accounts);
    }

    private Optional<Account> findAccount(String username) {
        return accounts.stream()
                .filter(a -> a.getUser().getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    // ---------- Login / account creation flow ----------

    private void showLoginFlow() {
        while (currentAccount == null) {
            String[] options = { "Login", "Create account", "Exit" };
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Log in to your account to continue.",
                    "Login",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == -1 || choice == 2) {
                System.exit(0);
            } else if (choice == 0) {
                handleLogin();
            } else if (choice == 1) {
                handleCreateAccount();
            }
        }
    }

    private void handleLogin() {
        String username = JOptionPane.showInputDialog(
                null,
                "Username:",
                "Login",
                JOptionPane.QUESTION_MESSAGE
        );
        if (username == null) {
            return;
        }
        username = username.trim();
        if (username.isEmpty()) {
            showMessage("Username is required.");
            return;
        }

        Optional<Account> accountOpt = findAccount(username);
        if (accountOpt.isEmpty()) {
            showMessage("No account with that username.");
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(
                null,
                passwordField,
                "Password for " + username,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        String password = new String(passwordField.getPassword());

        Account account = accountOpt.get();
        if (!account.getUser().checkPassword(password)) {
            showMessage("Wrong password.");
            return;
        }

        currentAccount = account;
        showMessage("Welcome, " + currentAccount.getUser().getUsername() + "!");
    }

    private void handleCreateAccount() {
        String username = JOptionPane.showInputDialog(
                null,
                "Choose a username:",
                "Create account",
                JOptionPane.QUESTION_MESSAGE
        );
        if (username == null) {
            return;
        }
        username = username.trim();
        if (username.isEmpty()) {
            showMessage("Username is required.");
            return;
        }
        if (findAccount(username).isPresent()) {
            showMessage("That username is already taken.");
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(
                null,
                passwordField,
                "Choose a password",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        String password = new String(passwordField.getPassword()).trim();
        if (password.isEmpty()) {
            showMessage("Password cannot be blank.");
            return;
        }

        User user = new User(username, password);
        Account account = new Account(user);
        accounts.add(account);
        saveAccounts();
        currentAccount = account;
        showMessage("Account created. You are now logged in as " + username + ".");
    }

    // ---------- UI setup ----------

    private void initUi() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Save accounts + portfolios when window closes
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAccounts();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));

        JPanel searchPanel = new JPanel(new BorderLayout(4, 4));
        JLabel symbolLabel = new JLabel("Symbol:");
        symbolField = new JTextField();
        symbolField.setToolTipText("Enter stock symbol, e.g., AAPL, MSFT");

        searchPanel.add(symbolLabel, BorderLayout.WEST);
        searchPanel.add(symbolField, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JButton quoteButton = new JButton("Get Quote");
        JButton historyButton = new JButton("View 5-Day History");
        JButton addButton = new JButton("Add to Portfolio");
        JButton portfolioButton = new JButton("View Portfolio");
        JButton clearButton = new JButton("Clear");

        buttonsPanel.add(quoteButton);
        buttonsPanel.add(historyButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(portfolioButton);
        buttonsPanel.add(clearButton);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        quoteButton.addActionListener(e -> onQuote());
        historyButton.addActionListener(e -> onHistory());
        addButton.addActionListener(e -> onAdd());
        portfolioButton.addActionListener(e -> onViewPortfolio());
        clearButton.addActionListener(e -> outputArea.setText(""));

        frame.getContentPane().setLayout(new BorderLayout(8, 8));
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setSize(900, 400);
        frame.setLocationRelativeTo(null);
    }

    private void updateWindowTitle() {
        String userPart = (currentAccount != null)
                ? " - " + currentAccount.getUser().getUsername()
                : "";
        if (frame != null) {
            frame.setTitle("Stock Market Interface" + userPart);
        }
    }

    // ---------- Button actions ----------

    private String readSymbol() {
        String symbol = symbolField.getText().trim().toUpperCase();
        if (symbol.isEmpty()) {
            showMessage("Please enter a stock symbol.");
            return null;
        }
        return symbol;
    }

    private void onQuote() {
        String symbol = readSymbol();
        if (symbol == null) return;

        String result = marketService.fetchAndFormatQuote(symbol);
        outputArea.setText(result);
    }

    private void onHistory() {
        String symbol = readSymbol();
        if (symbol == null) return;

        int days = 5;
        String summary = marketService.fetchAndFormatHistory(symbol, days);
        outputArea.setText(summary);

        try {
            PriceHistory history = marketService.fetchHistory(symbol, days);
            JDialog dialog = new JDialog(frame,
                    "5-Day Chart: " + history.getSymbol(),
                    false);
            dialog.getContentPane().add(new StockChartPanel(history));
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        } catch (IOException e) {
            showMessage("Error fetching history: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showMessage("Error processing history: " + e.getMessage());
        }
    }

    private void onAdd() {
        if (currentAccount == null) {
            showMessage("You must be logged in.");
            return;
        }

        String symbol = readSymbol();
        if (symbol == null) return;

        String sharesText = JOptionPane.showInputDialog(
                frame,
                "Number of shares to add for " + symbol + ":",
                "Add to Portfolio",
                JOptionPane.QUESTION_MESSAGE
        );
        if (sharesText == null) {
            return;
        }

        sharesText = sharesText.trim();
        if (sharesText.isEmpty()) {
            showMessage("Shares must be a whole number.");
            return;
        }

        try {
            int shares = Integer.parseInt(sharesText);
            currentAccount.getPortfolio().addShares(symbol, shares);
            outputArea.setText(
                    "Added " + shares + " shares of " + symbol + " to your portfolio."
            );
        } catch (NumberFormatException ex) {
            showMessage("Shares must be a whole number.");
        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage());
        }
    }

    private void onViewPortfolio() {
        if (currentAccount == null) {
            showMessage("You must be logged in.");
            return;
        }
        String text = portfolioService.formatPortfolio(currentAccount.getPortfolio());
        outputArea.setText(text);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FinanceGuiApp app = new FinanceGuiApp();
            app.start();   // <-- this is where login is forced
        });
    }
}
