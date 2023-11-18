package com.Niit.JukeBox.MainMenu;

import com.Niit.JukeBox.Controller.SongController;
import com.Niit.JukeBox.Model.Song;

import java.util.List;
import java.util.Scanner;

public class SongMenu {
    public SongController songController;
    public SongMenu(SongController songController) {
        this.songController = songController;
    }
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Song Menu");
            System.out.println("\t\t1. Add Song");
            System.out.println("\t\t2. Delete Song");
           // System.out.println("3. View All Songs Song Id");
            System.out.println("\t\t3. View All Songs  Name");
            System.out.println("\t\t4. search song by songname or artist name");
            System.out.println("\t\t5. Back to Main Menu");
            System.out.print("\t\tEnter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // newline character

            switch (choice) {
                case 1:
                    addSong();
                    break;
                case 2:
                    deleteSong();
                    break;
//                case 3:
//                    viewAllSongsById();
//                    break;
                case 3:
                   viewAllSongsByName();
                    break;
                case 4:
                    searchSongsByNameOrArtist();
                    break;
                case 5:
                    System.out.println("Returning to the main menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private void searchSongsByNameOrArtist() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the song name or artist name");
        String searchQuery=scanner.nextLine();
        //System.out.println(songController.searchSongs(searchQuery));
        List<Song> songname=songController.searchSongs(searchQuery);
        if (songname.isEmpty()) {
            System.out.println("No songs available.");
        }

        else {
            System.out.println("Artist name     Song name");
            for (Song song : songname) {
                System.out.println(song.getArtist()+" "+song.getName());
            }
        }
    }

    private void addSong() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter details for the new song:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Artist: ");
        String artist = scanner.nextLine();
        System.out.print("filepath: ");
        String Filepath = scanner.nextLine();
        // Add more fields

        Song newSong = new Song(0, name, artist, "", "", "", "mp3",Filepath);
        songController.addSong(newSong);
        System.out.println("Song added successfully!");
    }

    private void deleteSong() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the song to delete: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            System.out.print("Enter the ID of the song to delete: ");
            scanner.next();
        }
        int songID = scanner.nextInt();
        songController.deleteSong(songID);
        System.out.println("Song deleted successfully!");
    }

    private void viewAllSongsById() {
        List<Song> songs = songController.getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("No songs available.");
        } else {
            System.out.println("List of Song IDs:");

            for (Song song : songs) {
                System.out.println(song.getSongID());
            }
        }
    }
    private void viewAllSongsByName() {
        List<Song> songs = songController.getAllSongs(); // Get all songs from the database
        if (songs.isEmpty()) {
            System.out.println("No songs available.");
        } else {
            System.out.println("List of All Songs:");

            for (Song song : songs) {
                System.out.println(song.getSongID()+" "+song.getName());
            }
        }
    }
}
