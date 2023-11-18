package com.Niit.JukeBox.Credential;

import com.Niit.JukeBox.Connector.DatabaseConnector;
import com.Niit.JukeBox.Model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class UserCredential {
    private User user;
    private DatabaseConnector databaseConnector;
    private User currentUser;
    public UserCredential(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public UserCredential(User user, DatabaseConnector databaseConnector) {
        this.user = user;
        this.databaseConnector = databaseConnector;
    }

    public boolean isValidCredential(String username, String password) {
        // Fetch the user from the database and compare the provided credentials
        User fetchedUser = databaseConnector.getUserByUsername(username);

        return fetchedUser != null && fetchedUser.getPassword().equals(password);
    }

    public void registerUser(String username, String password) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Register");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        // Create a new user
        User newUser = new User(0, username, password, email);

        // Add the user to the database, and check for duplicates
        try {
            if (databaseConnector.addUser(newUser)) {
                System.out.println("Registration successful!");
                currentUser = newUser;
            } else {
                System.out.println("Username is already taken. Please choose a different username.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during registration. Please try again.");
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
