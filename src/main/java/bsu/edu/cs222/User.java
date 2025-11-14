package bsu.edu.cs222;

import java.io.Serializable;
import java.util.Scanner;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String userName;
    protected String password;

    public User(String userName, String password) { this.userName=userName; this.password=password; }

    public boolean login() {
        Scanner in = new Scanner(System.in);
        System.out.print("User name: ");
        String u = in.nextLine();
        if (!u.equals(this.userName)) { System.out.println("User name not found."); return false; }
        System.out.print("Password: ");
        String p = in.nextLine();
        if (!p.equals(this.password)) { System.out.println("Incorrect password."); return false; }
        System.out.println("Login successful!");
        return true;
    }

    public boolean login(String u, String p){ return u.equals(this.userName) && p.equals(this.password); }
    public void setUserName(String n){ this.userName=n; }
    public String getUserName(){ return this.userName; }
    public void setPassword(String p){ this.password=p; }
    public String getPassword(){ return this.password; }
}
