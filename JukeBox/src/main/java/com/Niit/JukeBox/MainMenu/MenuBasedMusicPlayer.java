package com.Niit.JukeBox.MainMenu;

import com.Niit.JukeBox.Connector.DatabaseConnector;
import com.Niit.JukeBox.Controller.MusicController;
import com.Niit.JukeBox.Controller.PlayListCrud;
import com.Niit.JukeBox.Controller.SongController;
import com.Niit.JukeBox.Credential.UserCredential;
import com.Niit.JukeBox.Model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuBasedMusicPlayer {
    private int authenticatedUserID;

    private User currentUser;
    private UserCredential userCredential;
    private MusicController musicController;
    private DatabaseConnector databaseConnector;
    private SongController songController;
    private SongMenu songMenu;
    private  PlaylistMenu playlistMenu;
    private PlayListCrud playListCrud;


    public MenuBasedMusicPlayer() {
        databaseConnector = new DatabaseConnector();
        userCredential = new UserCredential(databaseConnector);
        musicController = new MusicController(databaseConnector);
        songController = new SongController(databaseConnector);
        playListCrud = new PlayListCrud(databaseConnector);
        songMenu = new SongMenu(songController);
        playlistMenu = new PlaylistMenu(playListCrud, songController,musicController,authenticatedUserID);
    }

    public void start() {
        System.out.println("Welcome to My Jukebox");
        authenticateUser();

        int menuSelection;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("1. Play Song, 2. Resume Song, 3. Stop Song, 4. Next Song, 5.Song manipulation ,6.playList Manipulation,7. Exit");
            System.out.println("Enter your selection: ");

            menuSelection = scanner.nextInt();

            switch (menuSelection) {
                case 1:
                    // Get the path of the song from the user or database
                    String songPath = getSongPathFromUserOrDatabase();
                    musicController.play(songPath);
                    break;
                case 2:
                    musicController.resume();
                    break;
                case 3:
                    musicController.stop();
                    break;
                case 4:
                    musicController.next();
                    break;
                case 5:
                   songMenu.displayMenu();
                    break;
                case 6:
                    playlistMenu.displayMenu();
                    break;
                case 7:
                    musicController.stop();  // Stop the player before exiting
                    System.out.println("Exiting music player...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        } while (menuSelection != 7);
    }
    private void authenticateUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (!userCredential.isValidCredential(username, password)) {
            System.out.println("User not found. Do you want to register? (yes/no)");
            String registerChoice = scanner.nextLine().toLowerCase();

            if (registerChoice.equals("yes")) {
                // Register the new user
                userCredential.registerUser(username, password);
                System.out.println("Registration successful. Logging in...");
                authenticatedUserID=userCredential.getCurrentUser().getId();
            } else {
                System.out.println("Authentication failed. Exiting.");
                System.exit(0);
            }
        }
    }

    private String getSongPathFromUserOrDatabase() {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        try {
            String randomSongPath = databaseConnector.getRandomSongPath();
            if (randomSongPath != null) {
                return randomSongPath;
            } else {
                // If no song path is found in the database, return a default path
                return "SongData/Bardaan-Anha-San.mp3";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Return a default path in case of an error
            return "SongData/futuristic-beat.mp3";
        }
    }


    public static void main(String[] args) {
        MenuBasedMusicPlayer musicPlayer = new MenuBasedMusicPlayer();
        SongController songController=new SongController();
        String filepath="SongData";
        songController.addSongsFromFolder(filepath);
        musicPlayer.start();
    }
}
