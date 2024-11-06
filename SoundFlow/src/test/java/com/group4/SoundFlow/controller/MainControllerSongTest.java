package com.group4.SoundFlow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.SoundFlow.dao.ArtistRepo;
import com.group4.SoundFlow.dao.PlaylistRepo;
import com.group4.SoundFlow.dao.SongRepo;
import com.group4.SoundFlow.dto.Artist;
import com.group4.SoundFlow.dto.Playlist;
import com.group4.SoundFlow.dto.Song;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc // This annotation sets up MockMvc automatically
class MainControllerSongTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController controller;

    @Autowired
    private PlaylistRepo playlistRepo;

    @Autowired
    private SongRepo songRepo;

    @Autowired
    private ArtistRepo artistRepo;

    // Empty the testdb before each test
    @BeforeEach
    @Transactional
    public void setUp() {
        playlistRepo.deleteAll();
        playlistRepo.resetAutoIncrement();
        songRepo.deleteAll();
        songRepo.resetAutoIncrement();
        artistRepo.deleteAll();
        artistRepo.resetAutoIncrement();
    }


//    @Test
//    void CreatePlaylistAndGetAllTest() throws Exception {
//        // Send two playlist to DB
//        Playlist testPlaylist = new Playlist();
//        testPlaylist.setPlaylistName("Test Playlist name");
//        playlistRepo.save(testPlaylist);
//
//        Playlist testPlaylist2 = new Playlist();
//        testPlaylist2.setPlaylistName("Test Playlist name2");
//        playlistRepo.save(testPlaylist2);
//
//        mockMvc.perform(get("/playlists"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
//                .andExpect(jsonPath("$[*].playlistName", containsInAnyOrder("Test Playlist name", "Test Playlist name2")))
//                .andDo(print()); // optional: Print request & response details
//    }

    @Test
    void AddAndRemoveSongFromValidPlaylistTest() throws Exception {

        // Send a Playlist to DB
        Playlist testPlaylist = new Playlist();
        testPlaylist.setPlaylistName("Test Playlist name");
        testPlaylist = playlistRepo.save(testPlaylist);

        // Send an Artist to DB
        Artist testArtist = new Artist();
        testArtist.setName("The Weeknd");
        testArtist.setSpotifyId("abc123");
        testArtist.setImageUrl("https://example.com/image/the-weeknd");
        testArtist.setGenre("Pop");
        testArtist = artistRepo.save(testArtist);

        // Put the Artist in a list
        List<Artist> artistList = new ArrayList<>();
        artistList.add(testArtist);

        // Create a Song object with the ArtistLists
        Song song = new Song();
        song.setSongName("Test Song2");
        song.setSpotifyId("777bbb");
        song.setArtists(artistList);
        song.setPreviewUrl("https://example.com/preview/");
        song.setAlbum("After Hours");

        // Do a POST method and send the Song object in body
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/playlists/{playlistId}/songs", testPlaylist.getId() )
                .content(new ObjectMapper().writeValueAsString(song))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()); // expect CREATED status 201

        // Do a GET method to retrieve the playlist and validate that the song is there
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/playlists/{id}", testPlaylist.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(1)))
                .andExpect(jsonPath("$.songs[0].spotifyId").value(song.getSpotifyId()))
                .andExpect(jsonPath("$.songs[0].songName").value(song.getSongName()))
                .andExpect(jsonPath("$.songs[0].previewUrl").value(song.getPreviewUrl()))
                .andExpect(jsonPath("$.songs[0].album").value(song.getAlbum()));

        // Do a DELETE method and delete the Song (id=1) from playlist (id=1)
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/playlists/{playlistId}/songs/{songId}", testPlaylist.getId(), 1 ))
                .andExpect(status().isNoContent()); // expect NO CONTENT status

        // Do a GET method again to retrieve the playlist and validate that the songs list is empty
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/playlists/{id}", testPlaylist.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(0)));

    }

    @Test
    void AddSongToInvalidPlaylistTest() throws Exception {

        // Create an Artist object
        Artist testArtist = new Artist();
        testArtist.setName("The Weeknd");
        testArtist.setSpotifyId("abc123");
        testArtist.setImageUrl("https://example.com/image/the-weeknd");
        testArtist.setGenre("Pop");

        List<Artist> artistList = new ArrayList<>();
        artistList.add(testArtist);

        // Create a Song object with the ArtistLists
        Song song = new Song();
        song.setSongName("Test Song2");
        song.setSpotifyId("777bbb");
        song.setArtists(artistList);
        song.setPreviewUrl("https://example.com/preview/");
        song.setAlbum("After Hours");

        // Do a POST method and send a Song to an invalid playlistId
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/playlists/{playlistId}/songs", 1) // Non existing playlistId
                        .content(new ObjectMapper().writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()); // Except error 422
    }

    @Test
    void RemoveSongFromInvalidPlaylistTest() throws Exception {

        // Do a DELETE method and delete a song from an invalid playlist
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/playlists/{playlistId}/songs/{songId}", 5, 1 ))
                .andExpect(status().isUnprocessableEntity()); // expect error 422

    }

    @Test
    void RemoveInvalidSongFromPlaylistTest() throws Exception {
        // Send a Playlist to DB
        Playlist testPlaylist = new Playlist();
        testPlaylist.setPlaylistName("Test Playlist name");
        testPlaylist = playlistRepo.save(testPlaylist);

        // Do a DELETE method and delete an invalid song from a playlist
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/playlists/{playlistId}/songs/{songId}", testPlaylist.getId(), 5 ))
                .andExpect(status().isUnprocessableEntity()); // expect error 422

    }
}