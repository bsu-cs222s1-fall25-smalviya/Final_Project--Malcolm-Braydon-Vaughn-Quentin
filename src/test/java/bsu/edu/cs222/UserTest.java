package bsu.edu.cs222;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.File; // Import File

// Using JUnit 5 Assertions
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user; // We can still test against the User interface/abstract class
    private final InputStream standardIn = System.in;
    private static final String TEST_FILE_NAME = "accounts.dat"; // Use the same file name

    @BeforeEach
    void setUp(){

        // Instead of 'new ConcreteUser(...)', we now use 'new Account(...)'
        user = new Account("DanielFinances","PW1234");

        // Clean up any old test files before each test
        new File(TEST_FILE_NAME).delete();
    }

    @AfterEach
    void tearDown(){
        System.setIn(standardIn);
        // Clean up the test file after each test
        new File(TEST_FILE_NAME).delete();
    }

    @Test
    void constructorInitializesCorrectly(){
        assertEquals("DanielFinances", user.getUserName(), "Username should be initialized correctly.");
        assertEquals("PW1234", user.getPassword(), "Password should be initialized correctly.");
    }
    // Setting username and password updates
    @Test
    void setUsernameUpdateField(){
        user.setUserName("DanielFinances");
        assertEquals("DanielFinances", user.getUserName(), "Username should be updated to DanielFinances");
    }
    @Test
    void setPasswordUpdatesField(){
        user.setPassword("PW1234");
        assertEquals("PW1234", user.getPassword(), "Password should be updated to PW1234.");
    }
    // Automated Logins (Tests)
    @Test
    void automatedLogin_correctCredentials_returnsTrue(){
        assertTrue(user.login("DanielFinances","PW1234"),"Login with correct credentials should succeed.");
    }
    @Test
    void automatedLogin_incorrectPassword_returnFalse(){
        assertFalse(user.login("DanielFinances","PW12934"), "Login with incorrect password should fail.");
    }
    @Test
    void automatedLogin_incorrectUserName_returnsFalse(){
        assertFalse(user.login("DanielF","PW1234"), "Incorrect username");
    }
    @Test
    void automatedLogin_caseSensitiveUserName_returnsFalse(){
        assertFalse(user.login("Danielfinances","PW1234"), "Login is case sensitive for username.");
    }
    @Test void automatedLogin_caseSensitivePassword_returnsFalse(){
        assertFalse(user.login("DanielFinances","pw1234"), "Login is case sensitive for password");
    }

    private void mockSystemIn(String input){
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
    }

    @Test
    void consoleLogin_successfulLogin_returnsTrue(){
        mockSystemIn("DanielFinances\nPW1234\n");
        // We also need to fix the logic in User.java for this to pass
        assertTrue(user.login(), "Console login successful");
    }

    @Test
    void consoleLogin_incorrectPassword_returnsFalse(){
        mockSystemIn("DanielFinances\nWrongPassword\n");
        assertFalse(user.login(), "Console login incorrect password");
    }

    @Test
    void consoleLogin_incorrectUserName_returnsFalse(){
        mockSystemIn("DanielF\n");
        assertFalse(user.login(), "Console login incorrect username");
    }
}

