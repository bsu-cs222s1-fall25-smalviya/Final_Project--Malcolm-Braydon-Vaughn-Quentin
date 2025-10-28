package bsu.edu.cs222;

import java.io.Serializable;
import java.util.Scanner;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String userName;
    protected String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Interactive login (not required by API layer)
    public boolean login() {
        Scanner input = new Scanner(System.in);
        System.out.print("User name: ");
        String userNameIn = input.nextLine();
        System.out.print("Password: ");
        String passwordIn = input.nextLine();
        return login(userNameIn, passwordIn);
    }

    // Simple credential check used by Account.loginExisting(...)
    public boolean login(String userNameIn, String passwordIn) {
        return userNameIn.equals(this.userName) && passwordIn.equals(this.password);
    }

    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return this.userName; }

    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return this.password; }
}

