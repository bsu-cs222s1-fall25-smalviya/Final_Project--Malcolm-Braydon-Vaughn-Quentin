package bsu.edu.cs222;

import java.io.*;
import java.util.*;
import bsu.edu.cs222.model.Portfolio;

public class Account extends User {
    private static final long serialVersionUID = 1L;
    private static final String FILE_NAME = "accounts.dat";
    private static List<Account> allAccounts = new ArrayList<>();

    private final Portfolio portfolio = new Portfolio();
    public Account(String user, String pass){ super(user, pass); }
    public Portfolio portfolio(){ return portfolio; }

    public static Account createAccount(String u, String p) {
        Account a = new Account(u,p);
        allAccounts.add(a);
        saveAccounts();
        System.out.println("Account created successfully!");
        return a;
    }

    @SuppressWarnings("unchecked")
    public static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            allAccounts = (List<Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing accounts found, creating new list...");
            allAccounts = new ArrayList<>();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(allAccounts);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static Account loginExisting(String u, String p) {
        for (Account a : allAccounts) if (a.login(u,p)) {
            System.out.println("Welcome back, " + a.getUserName() + "!");
            return a;
        }
        System.out.println("Invalid login. Please try again.");
        return null;
    }
}
