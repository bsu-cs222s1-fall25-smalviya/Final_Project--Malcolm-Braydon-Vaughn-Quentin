package bsu.edu.cs222;

import javax.swing.*;

public class StockMarketGUI{
    private static final String VALID_USERNAME = "user123";
    private static final String VALID_PASSWORD = "password456";

    public static void main(String[] args) {

        String username = JOptionPane.showInputDialog(
                null,
                "Enter Username:",
                "Stock Market Login",
                JOptionPane.QUESTION_MESSAGE
        );
        if (username == null){
            JOptionPane.showMessageDialog(null, "Login cancelled. Exiting.");
            return;
        }

        String password = "password456";
        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)){

            JOptionPane.showMessageDialog(
                    null,
                    "Hello " + username + "! Login successful. Welcome to the stock Market.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Login failed. Invalid username or password.",
                    "Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
