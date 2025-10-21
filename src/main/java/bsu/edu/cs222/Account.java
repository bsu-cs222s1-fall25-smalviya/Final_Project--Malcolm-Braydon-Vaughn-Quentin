package bsu.edu.cs222;

import java.io.*;
import java.util.*;

public class Account extends User {
    private static final String FILE_NAME = "accounts.dat";
    private static List<Account> allAccounts = new ArrayList<>();

    public Account(String userName, String password) {
        super(userName, password);
    }

    // Create new account and save to file
    public static Account createAccount(String userName, String password) {
        Account account = new Account(userName, password);
        allAccounts.add(account);
        saveAccounts();
        System.out.println("Account created successfully!");
        return account;
    }

    // Load accounts from .dat file
    @SuppressWarnings("unchecked")
    public static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            allAccounts = (List<Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing accounts found, creating new list...");
            allAccounts = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Save accounts to .dat file
    public static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(allAccounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Attempt login by checking all stored accounts
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

    // Optional: Change password
    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        setPassword(newPassword);
        saveAccounts();
        System.out.println("Password changed successfully.");
    }

    // Example of displaying account info
    public String getReport() {
        return "User: " + this.getUserName();
    }

    // Simple test main
    public static void main(String[] args) {
        loadAccounts();
        Scanner input = new Scanner(System.in);

        System.out.print("1) Login or 2) Create account? ");
        int choice = input.nextInt();
        input.nextLine(); // consume newline

        System.out.print("Username: ");
        String user = input.nextLine();
        System.out.print("Password: ");
        String pass = input.nextLine();

        Account account = null;
        if (choice == 1) {
            account = loginExisting(user, pass);
        } else if (choice == 2) {
            account = createAccount(user, pass);
        }

        if (account != null) {
            System.out.println("Account loaded: " + account.getReport());
        }

        saveAccounts();
    }
}

