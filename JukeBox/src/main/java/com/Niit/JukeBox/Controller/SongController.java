package com.Niit.JukeBox.Controller;

import com.Niit.JukeBox.Connector.DatabaseConnector;
import com.Niit.JukeBox.Model.PlayList;
import com.Niit.JukeBox.Model.Song;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.Niit.JukeBox.Connector.DatabaseConnector.getConnection;

public class SongController {
    private Connection connection;
    private DatabaseConnector databaseConnector;
    private List<PlayList> playlists;

    public SongController(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
        this.connection = databaseConnector.getConnection();
    }


    public SongController() {
        this.connection = getConnection();
        this.databaseConnector = new DatabaseConnector();
    }


    public void addSongsFromFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            List<Song> songsToAdd = new ArrayList<>();

            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    String name = file.getName();
                    String artist = "Unknown";
                    String album = "Unknown";
                    String genre = "Unknown";
                    String duration = "Unknown";
                    String fileFormat = "mp3";
                    String path = file.getPath();

                    // Check if the song already exists in the database
                    Song existingSong = getSongByName(name);

                    if (existingSong == null) {
                        // Add the song to the list of songs to add
                        Song newSong = new Song(0, name, artist, album, genre, "", fileFormat, path);
                        songsToAdd.add(newSong);
                    } else {
                        // If the song already exists
                        existingSong.setFilePath(path);
                    }
                }
            }

            // Add songs to the database in batches
            addSongs(songsToAdd);
        }
    }




    public void addSong(Song song) {
        String query = "INSERT INTO Songs (SongID, Name, Artist, Album, Genre, Duration, FileFormat, FilePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, song.getSongID());
            preparedStatement.setString(2, song.getName());
            preparedStatement.setString(3, song.getArtist());
            preparedStatement.setString(4, song.getAlbum());
            preparedStatement.setString(5, song.getGenre());
            preparedStatement.setString(6, song.getDuration());
            preparedStatement.setString(7, song.getFileFormat());
             preparedStatement.setString(8, song.getFilePath());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addSongs(List<Song> songs) {
        String query = "INSERT INTO Songs (SongID, Name, Artist, Album, Genre, Duration, FileFormat, FilePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Song song : songs) {
                preparedStatement.setInt(1, song.getSongID());
                preparedStatement.setString(2, song.getName());
                preparedStatement.setString(3, song.getArtist());
                preparedStatement.setString(4, song.getAlbum());
                preparedStatement.setString(5, song.getGenre());
                preparedStatement.setString(6, song.getDuration());
                preparedStatement.setString(7, song.getFileFormat());
                preparedStatement.setString(8, song.getFilePath());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Song getSongByName(String name) {
        String query = "SELECT * FROM Songs WHERE Name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToSong(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void deleteSong(int songID) {
        String query = "DELETE FROM Songs WHERE SongID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, songID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Song> getAllSongs()  {
        List<Song> songs = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM songs";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Song song = mapResultSetToSong(resultSet);
                    songs.add(song);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return songs;
    }

    public String getSongPath(int songID) {
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT filepath FROM songs WHERE songId = ?")) {

            statement.setInt(1, songID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("filepath");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Song> searchSongs(String query) {
        List<Song> songs = new ArrayList<>();
        String searchQuery = "SELECT * FROM Songs WHERE Name LIKE ? OR Artist LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
            preparedStatement.setString(1, "%" + query + "%");
            preparedStatement.setString(2, "%" + query + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                songs.add(mapResultSetToSong(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return songs;
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

}


