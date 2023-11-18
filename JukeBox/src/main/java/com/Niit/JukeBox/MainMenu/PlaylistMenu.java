package com.Niit.JukeBox.MainMenu;



import com.Niit.JukeBox.Controller.MusicController;
import com.Niit.JukeBox.Controller.PlayListCrud;
import com.Niit.JukeBox.Controller.SongController;
import com.Niit.JukeBox.Model.PlayList;
import com.Niit.JukeBox.Model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaylistMenu {
    private PlayListCrud playListCrud;
    private Scanner scanner;
    private SongController songController;
    private MusicController musicController;
    private int authenticatedUserID;

    public PlaylistMenu(PlayListCrud playListCrud, SongController songController, MusicController musicController,int authenticatedUserID) {
        this.playListCrud = playListCrud;
        this.songController = songController;
        this.musicController = musicController; // Initialize the musicController
        this.scanner = new Scanner(System.in);
        this.authenticatedUserID = authenticatedUserID;
    }

    public void displayMenu() {
        int playlistChoice;
        do {
            System.out.println("Playlist Menu");
            System.out.println("\t\t1. Create Playlist");
            System.out.println("\t\t2. Add Song to Playlist");
            System.out.println("\t\t3. View Playlist Songs");
            System.out.println("\t\t4. View Playlist Name");
            System.out.println("\t\t5. play playlist song ");
            System.out.println("\t\t6. delete playlist by Id");
            System.out.println("\t\t7. search  playlist by name");
            System.out.println("\t\t8. Back to Main Menu");
            System.out.print("\t\tEnter your choice: ");
            playlistChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (playlistChoice) {
                case 1:
                    createPlaylist();
                    break;
                case 2:
                    addSongToPlaylist();
                    break;
                case 3:
                    viewPlaylistSongs();
                    break;
                case 4:
                    viewPlaylistName();
                    break;
                case 5:
                   playSongsFromPlaylist();
                    break;
                case 6:
                   deletePlayListById();
                    break;
                case 7:
                    searchPlayList();
                    break;
                case 8:
                    System.out.println("Returning to the main menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (playlistChoice != 8);
    }

    private void searchPlayList() {
        System.out.println("enter the playlist name");
        String query=scanner.nextLine();
        List<PlayList> playLists=playListCrud.searchPlaylists(query);
        System.out.println("Available playlist name");
        for(PlayList p :playLists) {
            System.out.println(p.getName());
        }
        //playListCrud.searchPlaylists(query);
    }


    private void createPlaylist() {
        Scanner scanner = new Scanner(System.in);

        // Check if there are any existing playlists
        List<PlayList> playlists = playListCrud.getAllPlaylists();
        if (!playlists.isEmpty()) {
            System.out.println("Existing Playlists:");
            for (PlayList playlist : playlists) {
                System.out.println("Playlist ID: " + playlist.getPlaylistID() + ", Name: " + playlist.getName());
            }
        }

        // Get the name for the new playlist
        System.out.print("Enter the name of the playlist: ");
        String playlistName = scanner.nextLine();

        // Check if the playlist name already exists
        if (playlistNameExists(playlists, playlistName)) {
            System.out.println("Playlist with the name '" + playlistName + "' already exists.");
        } else {
            // Create the new playlist with the authenticated user's ID
            PlayList newPlaylist = new PlayList(0, playlistName, authenticatedUserID);
            playListCrud.addPlaylist(newPlaylist);
            System.out.println("Playlist created successfully!");
        }
    }

    private boolean playlistNameExists(List<PlayList> playlists, String playlistName) {
        for (PlayList playlist : playlists) {
            if (playlist.getName().equalsIgnoreCase(playlistName)) {
                return true;
            }
        }
        return false;
    }

    private void addSongToPlaylist() {
        Scanner scanner = new Scanner(System.in);

        // Display all available songs
        List<Song> allSongs = songController.getAllSongs();
        if (allSongs.isEmpty()) {
            System.out.println("No songs available. Please add songs first.");
            return;
        }

        System.out.println("List of Available Songs:");
        for (Song song : allSongs) {
            System.out.println("Song ID: " + song.getSongID() + ", SongName: " + song.getName() + ", Artist: " + song.getArtist());
        }

        System.out.print("Enter the ID of the playlist: ");
        int playlistID;
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            System.out.print("Enter the ID of the playlist: ");
            scanner.next();
        }
        playlistID = scanner.nextInt();
        scanner.nextLine(); //  newline character

        // Check if the playlist ID is valid
        PlayList playlist = playListCrud.getPlaylistById(playlistID);
        if (playlist != null) {

            System.out.print("Enter the ID of the song to add: ");
            int songID;
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                System.out.print("Enter the ID of the song to add: ");
                scanner.next();
            }
            songID = scanner.nextInt();
            scanner.nextLine(); //  newline character

            // Add the selected song to the playlist
            playListCrud.addSongToPlaylist(playlistID, songID);
            System.out.println("Song added to playlist successfully!");
        } else {
            System.out.println("Invalid playlist ID. Please enter a valid playlist ID.");
        }
    }



    private void viewPlaylistSongs() {
        // Get playlist ID
        System.out.print("Enter the ID of the playlist or playlist name: ");
        String playlistIdentifier = scanner.next();
        scanner.nextLine(); // newline character
        int playlistID = getPlaylistID(playlistIdentifier);
        // Retrieve and display playlist songs
        PlayList playlist = playListCrud.getPlaylistById(playlistID);

        if (playlist != null) {
            // Check if the authenticated user is the owner of the playlist
            if (playlist.getUserID() == authenticatedUserID) {
                List<Song> songs = playListCrud.getSongsByPlaylist(playlistID);
                if (songs.isEmpty()) {
                    System.out.println("No songs available in the playlist.");
                } else {
                    System.out.println("List of Songs in the Playlist:");
                    for (Song song : songs) {
                        System.out.println(song.getSongID()+" "+song.getName());
                    }
                }
            } else {
                System.out.println("You are not the owner of this playlist.");
            }
        } else {
            System.out.println("Playlist not found.");
        }
    }

    public void playSongsFromPlaylist() {
        viewPlaylistName(); // Display playlists
        System.out.print("Enter the ID or name of the playlist to play songs from: ");
        String playlistIdentifier = scanner.next();

        // Get the playlist ID based on the identifier (ID or name)
        int playlistID = getPlaylistID(playlistIdentifier);

        if (playlistID != -1) {
            // Display songs in the selected playlist
            List<Song> songsInPlaylist = playListCrud.getSongsByPlaylist(playlistID);
            if (!songsInPlaylist.isEmpty()) {
                System.out.println("Songs in the playlist:");
                for (Song song : songsInPlaylist) {
                    System.out.println("Song ID: " + song.getSongID() + ", SongName: " + song.getName());
                }

                // enter the user to select a song
                System.out.print("Enter the ID of the song to play: ");
                int selectedSongID = scanner.nextInt();

                // Get the path of the selected song
                String songPath = songController.getSongPath(selectedSongID);

                // Play the selected song
                musicController.play(songPath);
            } else {
                System.out.println("No songs available in the selected playlist.");
            }
        } else {
            System.out.println("Playlist not found.");
        }
    }
    private void deletePlayListById() {
        System.out.println("Enter the playList Id");
        int playlistId=scanner.nextInt();
        playListCrud.deletePlaylist(playlistId);
    }

    private int getPlaylistID(String playlistIdentifier) {
        try {
            // Try parsing the identifier as an integer (assuming it's a playlist ID)
            return Integer.parseInt(playlistIdentifier);
        } catch (NumberFormatException e) {
            // If parsing as an integer fails, assume it's a playlist name
            return playListCrud.getPlaylistIDByName(playlistIdentifier);
        }
    }

    private void viewPlaylistName() {
        List<PlayList> playlists = playListCrud.getAllPlaylists();
        if (playlists.isEmpty()) {
            System.out.println("No playlists available.");
        } else {
            System.out.println("List of Playlists:");
            for (PlayList playlist : playlists) {
                if (playlist.getUserID()== authenticatedUserID) {
                    System.out.println(playlist.getPlaylistID() + ". " + playlist.getName());
                }
            }
        }
    }


}

