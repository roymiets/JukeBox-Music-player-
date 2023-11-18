package com.Niit.JukeBox.Model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        int id = 1;
        String username = "AbhishekUser";
        String password = "APassword";
        String email = "Abhishek@outlook.com";

        // Act
        User user = new User(id, username, password, email);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());

        // Modify values using setters
        int newId = 2;
        String newUsername = "Abhishek";
        String newPassword = "Password";
        String newEmail = "Abhishek@gmail.com";

        user.setId(newId);
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        user.setEmail(newEmail);

        // Assert after modification
        assertEquals(newId, user.getId());
        assertEquals(newUsername, user.getUsername());
        assertEquals(newPassword, user.getPassword());
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    public void testToString() {
        // Arrange
        int id = 1;
        String username = "AbhishekUser";
        String password = "APassword";
        String email = "coderAk@gmail.com";
        User user = new User(id, username, password, email);

        // Act
        String toStringResult = user.toString();

        // Assert
        assertTrue(toStringResult.contains("User"));
        assertTrue(toStringResult.contains("id=" + id));
        assertTrue(toStringResult.contains("username='" + username + "'"));
        assertTrue(toStringResult.contains("password='" + password + "'"));
        assertTrue(toStringResult.contains("email='" + email + "'"));
    }
}
