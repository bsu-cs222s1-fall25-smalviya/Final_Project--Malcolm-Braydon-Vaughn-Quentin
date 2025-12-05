package bsu.edu.cs222;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class aUserTest {

    private User user;

    @BeforeEach
    void setUp() {
        // Match your current User constructor:
        // User(String username, String password)
        user = new User("DanielFinances", "PW1234");
    }

    @Test
    void constructorStoresUsername() {
        assertEquals("DanielFinances",
                user.getUsername(),
                "Username should be initialized correctly");
    }

    @Test
    void checkPasswordReturnsTrueForCorrectPassword() {
        assertTrue(user.checkPassword("PW1234"),
                "checkPassword should return true for the correct password");
    }

    @Test
    void checkPasswordReturnsFalseForWrongPassword() {
        assertFalse(user.checkPassword("wrongPW"),
                "checkPassword should return false for an incorrect password");
    }

    @Test
    void checkPasswordIsCaseSensitive() {
        assertFalse(user.checkPassword("pw1234"),
                "Password comparison should be case sensitive");
    }
}
