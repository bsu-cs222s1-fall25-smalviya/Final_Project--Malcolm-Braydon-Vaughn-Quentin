package bsu.edu.cs222;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class PortfolioStorage {

    private static final String PORTFOLIO_DIR = "portfolios";

    private PortfolioStorage() {}


    public static void loadPortfolios(List<Account> accounts) {
        File dir = new File(PORTFOLIO_DIR);
        if (!dir.exists()) {
            return; // no portfolios to load yet
        }

        for (Account account : accounts) {
            File file = new File(dir, account.getUser().getUsername() + ".pf");
            if (!file.exists()) {
                continue;
            }

            Portfolio portfolio = account.getPortfolio();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || !line.contains(",")) continue;

                    String[] parts = line.split(",");
                    if (parts.length != 2) continue;

                    String symbol = parts[0].trim().toUpperCase();
                    String qtyStr = parts[1].trim();

                    try {
                        int shares = Integer.parseInt(qtyStr);
                        if (shares > 0) {
                            portfolio.addShares(symbol, shares);
                        }
                    } catch (IllegalArgumentException e) {
                        // Covers NumberFormatException and invalid share counts
                        // Corrupt line — skip it
                    }
                }

            } catch (IOException e) {
                System.err.println("Error reading portfolio for "
                        + account.getUser().getUsername() + ": " + e.getMessage());
            }
        }
    }


      //Saves all account portfolios to disk.

    public static void savePortfolios(List<Account> accounts) {

        File dir = new File(PORTFOLIO_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        for (Account account : accounts) {
            File file = new File(dir, account.getUser().getUsername() + ".pf");

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

                for (Map.Entry<String, Integer> entry :
                        account.getPortfolio().getHoldings().entrySet()) {

                    writer.println(entry.getKey() + "," + entry.getValue());
                }

            } catch (IOException e) {
                System.err.println("Error saving portfolio for "
                        + account.getUser().getUsername() + ": " + e.getMessage());
            }
        }
    }
}
