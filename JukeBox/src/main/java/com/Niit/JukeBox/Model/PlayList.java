package com.Niit.JukeBox.Model;

import com.Niit.JukeBox.Model.Song;

import java.util.ArrayList;
import java.util.List;

public class PlayList {
    private int playlistID;
    private String name;
    private List<Song> songs;
    private int UserID;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public PlayList() {
        this.songs = new ArrayList<>();
    }


    public PlayList(int PlaylistID, String Name, int UserID) {
        this.playlistID = PlaylistID;
        this.name = Name;
        this.UserID = UserID;
        this.songs = new ArrayList<>();
    }

    public PlayList(int playlistID, String name, List<Song> songs) {
        this.playlistID = playlistID;
        this.name = name;
        this.songs = songs;
    }


    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

}
