package com.Niit.JukeBox.Controller;

import com.Niit.JukeBox.Connector.DatabaseConnector;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class MusicController {
    private AdvancedPlayer player;
    private DatabaseConnector connect;
    private String currentFilePath;


    public MusicController(DatabaseConnector databaseConnector) {
        this.connect = databaseConnector;
    }

    public void play(String filePath) {
        currentFilePath = filePath;
        try {

            FileInputStream fileInputStream = new FileInputStream(filePath);
            player = new AdvancedPlayer(fileInputStream);
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                }
            });

            new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }
    public void stop() {
        if (player != null) {
            player.close();
        }
    }

    public void resume() {
        if (currentFilePath != null) {
            play(currentFilePath);
        }
    }
    public void next() {
        // Stop the current song
        if (player != null) {
            player.close();
        }

        String nextSongPath = null;
        try {
            nextSongPath = connect.getNextSongPath(currentFilePath);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Play the next song, if there is one
        if (nextSongPath != null) {
            play(nextSongPath);
        }
    }
}
