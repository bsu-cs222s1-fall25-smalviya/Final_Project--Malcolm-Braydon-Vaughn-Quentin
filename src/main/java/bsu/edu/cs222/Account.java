package bsu.edu.cs222;

import bsu.edu.cs222.model.Portfolio;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Account extends User {
    private static final long serialVersionUID = 1L;

    private static final String FILE_NAME = "accounts.dat";
    private static List<Account> allAccounts = new ArrayList<>();

    // --- new field ---
    private Portfolio portfolio = new Portfolio();

    public Account(String userName, String password) {
        super(userName, password);
    }

    // --- persistence ---
    @SuppressWarnings("unchecked")
    public static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            allAccounts = (List<Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing accounts found, creating new list...");
            allAccounts = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            allAccounts = new ArrayList<>();
        }
    }

    public static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(allAccounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- account operations ---
    public static Account createAccount(String userName, String password) {
        Account account = new Account(userName, password);
        allAccounts.add(account);
        saveAccounts();
        System.out.println("Account created successfully!");
        return account;
    }

    public static Account loginExisting(String userName, String password) {
        for (Account acc : allAccounts) {
            if (acc.login(userName, password)) {
                System.out.println("Welcome back, " + acc.getUserName() + "!");
                return acc;
            }
        }
        System.out.println("Invalid login. Please try again.");
        return null;
    }

    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        setPassword(newPassword);
        saveAccounts();
        System.out.println("Password changed successfully.");
    }

    // --- portfolio access ---
    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    // --- report ---
    public String getReport() {
        return "User: " + this.getUserName() + "\n" + portfolio.toString();
    }

    // Optional: helper to find all accounts (for debugging/admin use)
    public static List<Account> getAllAccounts() {
        return allAccounts;
    }
}
