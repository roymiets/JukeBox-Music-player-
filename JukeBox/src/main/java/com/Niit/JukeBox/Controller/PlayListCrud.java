package com.Niit.JukeBox.Controller;

import com.Niit.JukeBox.Connector.DatabaseConnector;
import com.Niit.JukeBox.Model.PlayList;
import com.Niit.JukeBox.Model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.logger;

public class PlayListCrud {
    private Connection connection;
    public  DatabaseConnector databaseConnector;

    public PlayListCrud(DatabaseConnector databaseConnector) {
        this.connection = databaseConnector.getConnection();
    }

    public void addPlaylist(PlayList playlist) {
        String query = "INSERT INTO Playlists (PlaylistID, Name, UserID) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playlist.getPlaylistID());
            preparedStatement.setString(2, playlist.getName());
            preparedStatement.setInt(3, playlist.getUserID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public List<PlayList> getAllPlaylists() {
        List<PlayList> playlists = new ArrayList<>();
        String query = "SELECT * FROM Playlists";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                playlists.add(mapResultSetToPlaylist(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }


    public void addSongToPlaylist(int playlistID, int songID) {
        String query = "INSERT INTO PlaylistSongs (PlaylistID, SongID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playlistID);
            preparedStatement.setInt(2, songID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Song> getSongsByPlaylist(int playlistID) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT * FROM PlaylistSongs JOIN Songs ON PlaylistSongs.SongID = Songs.SongID WHERE PlaylistID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playlistID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                songs.add(mapResultSetToSong(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }


    public PlayList getPlaylistById(int playlistID) {
        String query = "SELECT * FROM Playlists WHERE PlaylistID = ?";
        try (Connection connection = databaseConnector.getConnection();PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playlistID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToPlaylist(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deletePlaylist(int playlistID) {
        String query = "DELETE FROM Playlists WHERE PlaylistID = ?; DELETE FROM PlaylistSongs WHERE PlaylistID = ?";
        try (Connection connector = databaseConnector.getConnection(); PreparedStatement statement = connector.prepareStatement(query)) {
            statement.setInt(1, playlistID);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error in deleting");
        }
    }


    public int getPlaylistIDByName(String playlistName) {
        try (Connection connection1 = databaseConnector.getConnection();
             PreparedStatement statement = connection1.prepareStatement("SELECT PlaylistID FROM Playlists WHERE Name = ?")) {

            statement.setString(1, playlistName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("PlaylistID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public List<PlayList> searchPlaylists(String query) {
        List<PlayList> playlists = new ArrayList<>();
        String searchQuery = "SELECT * FROM Playlists WHERE name LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
            preparedStatement.setString(1, "%" + query + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                playlists.add(mapResultSetToPlaylist(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playlists;
    }
    private Song mapResultSetToSong(ResultSet resultSet) throws SQLException {
        return new Song(
                resultSet.getInt("SongID"),
                resultSet.getString("Name"),
                resultSet.getString("Artist"),
                resultSet.getString("Album"),
                resultSet.getString("Genre"),
                resultSet.getString("Duration"),
                resultSet.getString("FileFormat"),
                resultSet.getString("FilePath")
        );
    }
    private PlayList mapResultSetToPlaylist(ResultSet resultSet) throws SQLException {
        return new PlayList(
                resultSet.getInt("PlaylistID"),
                resultSet.getString("Name"),
                // You may want to fetch and set other properties if needed
                new ArrayList<>() // Initialize an empty list for song IDs
        );
    }
}
