package bsu.edu.cs222;

import java.io.Serializable;
import java.util.Scanner;

public abstract class User implements Serializable {
    protected String userName;
    protected String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public boolean login() {
        Scanner input = new Scanner(System.in);
        System.out.print("User name: ");
        String userNameIn = input.nextLine();

        if (userNameIn.equals(this.userName)) {
            System.out.print("Password: ");
            String passwordIn = input.nextLine();
            if (passwordIn.equals(this.password)) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Incorrect password.");
            }
        } else {
            System.out.println("User name not found.");
        }
        return false;
    }

    public boolean login(String userNameIn, String passwordIn) {
        return userNameIn.equals(this.userName) && passwordIn.equals(this.password);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }
}

