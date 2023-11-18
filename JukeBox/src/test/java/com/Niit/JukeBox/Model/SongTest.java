package com.Niit.JukeBox.Model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SongTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        int songID = 1;
        String name = "Test Song";
        String artist = "Test Artist";
        String album = "Test Album";
        String genre = "Test Genre";
        String duration = "00:03:30";
        String fileFormat = "mp3";
        String filePath = "test/path/to/song.mp3";

        // Act
        Song song = new Song(songID, name, artist, album, genre, duration, fileFormat, filePath);

        // Assert
        assertEquals(songID, song.getSongID());
        assertEquals(name, song.getName());
        assertEquals(artist, song.getArtist());
        assertEquals(album, song.getAlbum());
        assertEquals(genre, song.getGenre());
        assertEquals(duration, song.getDuration());
        assertEquals(fileFormat, song.getFileFormat());
        assertEquals(filePath, song.getFilePath());

        // Modify values using setters
        int newSongID = 2;
        String newName = "New Song";
        String newArtist = "New Artist";
        String newAlbum = "New Album";
        String newGenre = "New Genre";
        String newDuration = "00:04:00";
        String newFileFormat = "wav";
        String newFilePath = "new/path/to/song.wav";

        song.setSongID(newSongID);
        song.setName(newName);
        song.setArtist(newArtist);
        song.setAlbum(newAlbum);
        song.setGenre(newGenre);
        song.setDuration(newDuration);
        song.setFileFormat(newFileFormat);
        song.setFilePath(newFilePath);

        // Assert after modification
        assertEquals(newSongID, song.getSongID());
        assertEquals(newName, song.getName());
        assertEquals(newArtist, song.getArtist());
        assertEquals(newAlbum, song.getAlbum());
        assertEquals(newGenre, song.getGenre());
        assertEquals(newDuration, song.getDuration());
        assertEquals(newFileFormat, song.getFileFormat());
        assertEquals(newFilePath, song.getFilePath());
    }

    @Test
    public void testToString() {
        // Arrange
        int songID = 1;
        String name = "Test Song";
        String artist = "Test Artist";
        String album = "Test Album";
        String genre = "Test Genre";
        String duration = "00:03:30";
        String fileFormat = "mp3";
        String filePath = "test/path/to/song.mp3";
        Song song = new Song(songID, name, artist, album, genre, duration, fileFormat, filePath);

        // Act
        String toStringResult = song.toString();

        // Assert
        assertTrue(toStringResult.contains("Song"));
        assertTrue(toStringResult.contains("songID=" + songID));
        assertTrue(toStringResult.contains("name='" + name + "'"));
        assertTrue(toStringResult.contains("artist='" + artist + "'"));
        assertTrue(toStringResult.contains("album='" + album + "'"));
        assertTrue(toStringResult.contains("genre='" + genre + "'"));
        assertTrue(toStringResult.contains("duration='" + duration + "'"));
        assertTrue(toStringResult.contains("fileFormat='" + fileFormat + "'"));
        assertTrue(toStringResult.contains("filePath='" + filePath + "'"));
    }
}

