package bsu.edu.cs222;

import java.util.Objects;

public class Account {

    private final User user;
    private final Portfolio portfolio;

    public Account(User user) {
        this.user = user;
        this.portfolio = new Portfolio(user.getUsername());
    }

    public User getUser() {
        return user;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Serialize this account to a simple line: username|password
     * This is what we store in accounts.dat.
     */
    public String serialize() {
        return user.getUsername() + "|" + user.getPassword();
    }

    public static Account fromLine(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Line is empty");
        }
        String[] parts = line.split("\\|");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Line must be username|password");
        }
        User user = new User(parts[0].trim(), parts[1].trim());
        return new Account(user);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Account)) return false;
        Account that = (Account) other;
        return Objects.equals(user.getUsername(), that.user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUsername());
    }
}
