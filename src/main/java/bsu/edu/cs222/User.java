package bsu.edu.cs222;

public class User {

    private final String username;
    private String password;

    public User(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    // Needed for Account serialization
    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String attempt) {
        if (attempt == null) {
            return false;
        }
        return password.equals(attempt);
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
        this.password = newPassword;
    }
}
