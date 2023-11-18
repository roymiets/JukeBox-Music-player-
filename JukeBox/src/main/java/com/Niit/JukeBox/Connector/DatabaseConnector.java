package com.Niit.JukeBox.Connector;

import com.Niit.JukeBox.Model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnector {
    private  Connection connection;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/jukebox_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Sanghi@12345";
    public DatabaseConnector() {
        this.connection = getConnection(); // Initialize the connection
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
    public String getNextSongPath(String currentSongPath) throws SQLException {
        try (
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT filepath FROM songs WHERE SongId = (SELECT SongId + 1 FROM songs WHERE filepath = ?)"
                )
        ) {
            preparedStatement.setString(1, currentSongPath);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                String nextSongPath = null;
                if (resultSet.next()) {
                    nextSongPath = resultSet.getString("filepath");
                }

                return nextSongPath;
            }
        }
    }

    public String getRandomSongPath() throws SQLException {
        String query = "SELECT FilePath FROM Songs ORDER BY RAND() LIMIT 1";
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("FilePath");
                }
            }
        }
        return null; // if no song path is found
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM User WHERE Username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addUser(User user) throws SQLException {
        String query = "INSERT INTO User (UserID, Username, Password, Email) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        // Map the ResultSet to a User object
        return new User(
                resultSet.getInt("userID"),
                resultSet.getString("Username"),
                resultSet.getString("Password"),
                resultSet.getString("Email")
        );
    }

}
