package com.group4.SoundFlow.controller;

import com.group4.SoundFlow.dao.ArtistRepo;
import com.group4.SoundFlow.dao.PlaylistRepo;
import com.group4.SoundFlow.dao.SongRepo;
import com.group4.SoundFlow.dto.Artist;
import com.group4.SoundFlow.dto.Playlist;
import com.group4.SoundFlow.dto.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class MainController {

    @Autowired
    PlaylistRepo playlistRepo;

    @Autowired
    SongRepo songRepo;

    @Autowired
    ArtistRepo artistRepo;


    // TODO: Probably will need to change the methods to send data to the front end, like in the 'Spring Boot with JPA' code-along
    // ==========================
    //          PLAYLIST
    // ==========================

    // This page displays the homepage - todo when we connect the front-end
    /*
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
    */

    // This page displays all Playlists
    @GetMapping("/playlists")
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        List<Playlist> playlistList = playlistRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(playlistList);
    }

    // This page displays a specific playlist
    @GetMapping("/playlists/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable("id") Integer id) {
        Playlist playlist = playlistRepo.findById(id).orElse(null);
        return ResponseEntity.status(HttpStatus.OK).body(playlist);
    }

    @PostMapping("/playlists")
    public ResponseEntity<Void> addNewPlaylist(@RequestBody Playlist playlist) {
        playlistRepo.save(playlist);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping("/playlists/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable("id") Integer id, @RequestBody Playlist playlist) {
        // Check if the playlist exists
        Playlist existingPlaylist = playlistRepo.findById(id).orElse(null);
        if (existingPlaylist == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // return error 422 if Playlist does not exist
        }

        // Update only the name field of the existing playlist
        existingPlaylist.setPlaylistName(playlist.getPlaylistName());

        // Save the updated playlist back to the database
        playlistRepo.save(existingPlaylist);

        // Return the updated playlist with a 200 OK status
        return new ResponseEntity<>(existingPlaylist, HttpStatus.OK);
    }

    @DeleteMapping("/playlists/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable("id") Integer id) {
        playlistRepo.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }


    // ==========================
    //          SONG
    // ==========================

    @GetMapping("/songs")
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songList = songRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(songList);
    }

    // This page is where we display a specific Song from a Playlist
    @GetMapping("/playlists/{playlistId}/songs/{songId}")
    public ResponseEntity<Song> getSongById(@PathVariable("playlistId") Integer playlistId,
                                            @PathVariable("songId") Integer songId) {
        Song song = songRepo.findByPlaylistIdAndSongId(playlistId, songId).orElse(null);
        return ResponseEntity.status(HttpStatus.OK).body(song);
    }

    // To add a Song to a Playlist
    @PostMapping("/playlists/{playlistId}/songs")
    public ResponseEntity<Void> addNewSong(@RequestBody Song song,
                                           @PathVariable("playlistId") Integer playlistId) {

        // Check if the playlist exists
        Playlist playlist = playlistRepo.findById(playlistId).orElse(null);
        if (playlist == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // return error 422 if Playlist does not exist
        }

        // List to hold the existing artists
        List<Artist> artistsToAdd = new ArrayList<>();

        // Check each artist in the request
        for (Artist artist : song.getArtists()) {
            // Check if the artist exists in the database
            Artist existingArtist = artistRepo.findBySpotifyId(artist.getSpotifyId()).orElse(null);

            // If the artist does not exist, create a new artist record
            if (existingArtist == null) {
                existingArtist = artistRepo.save(artist); // Save the new artist to the database
            }
            // Add the existing or newly created artist to the list
            artistsToAdd.add(existingArtist);
        }

        // Set the artists for the song
        song.setArtists(artistsToAdd);

        // Check if the song exists in the database
        Song existingSong = songRepo.findBySpotifyId(song.getSpotifyId()).orElse(null); // Use the Spotify ID or another unique identifier

        // If the song does not exist in the database, save the new song
        if (existingSong == null) {
            existingSong = songRepo.save(song); // Save the new song to the database
        }

        // Check if the song is already in the playlist
        if (!playlist.getSongs().contains(existingSong)) {
            playlist.getSongs().add(existingSong); // Add the existing song to the playlist
            playlistRepo.save(playlist); // Save the updated playlist
        }

        return new ResponseEntity<>(HttpStatus.CREATED); // return status 201

    }

    // To delete a Song from a Playlist
    @DeleteMapping("/playlists/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> deleteSong(@PathVariable("playlistId") Integer playlistId,
                                           @PathVariable("songId") Integer songId) {

        // Check if the playlist exists
        Playlist playlist = playlistRepo.findById(playlistId).orElse(null);
        if (playlist == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // return error 422 if Playlist does not exist
        }

        // Find the song
        Song song = songRepo.findById(songId).orElse(null);
        if (song == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // return error 422 if Song does not exist
        }

        // Else, Remove the song from the playlist
        playlist.getSongs().remove(song);
        playlistRepo.save(playlist); // persist the change in Playlist to DB

        // Check if the song is used in other playlists
        boolean isSongInOtherPlaylists = playlistRepo.countBySongsContaining(song) > 0;

        // If the song is not in any other playlists, delete it
        if (!isSongInOtherPlaylists) {
            // Remove associated artists if they are not used by any other song
            for (Artist artist : song.getArtists()) {
                boolean isArtistInOtherSongs = artistRepo.countBySongsContainingExcludingSong(artist.getId(), song.getId()) > 0;
                if (!isArtistInOtherSongs) {
                    artistRepo.delete(artist);
                }
            }
            // Now delete the song itself
            songRepo.delete(song);
        }

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }


    // ==========================
    //          ARTIST
    // ==========================

    // This page is where we display a specific Artist for a Song
    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable("id") Integer id) {
        Artist artist = artistRepo.findById(id).orElse(null);
        return ResponseEntity.status(HttpStatus.OK).body(artist);
    }

}
