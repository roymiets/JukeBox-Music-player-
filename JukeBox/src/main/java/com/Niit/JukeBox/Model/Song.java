package com.Niit.JukeBox.Model;

public class Song {
    private int songID;
    private String name;
    private String artist;
    private String album;
    private String genre;
    private String duration;
    private String fileFormat;
    private String filePath;

    public Song(int songID, String name, String artist, String album, String genre, String duration, String fileFormat,String filePath) {
        this.songID = songID;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.duration = duration;
        this.fileFormat = fileFormat;
        this.filePath = filePath;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songID=" + songID +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", duration='" + duration + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
