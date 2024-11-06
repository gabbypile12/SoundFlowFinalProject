package com.group4.SoundFlow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.SoundFlow.dao.ArtistRepo;
import com.group4.SoundFlow.dao.PlaylistRepo;
import com.group4.SoundFlow.dao.SongRepo;
import com.group4.SoundFlow.dto.Artist;
import com.group4.SoundFlow.dto.Playlist;
import com.group4.SoundFlow.dto.Song;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc // This annotation sets up MockMvc automatically
class MainControllerPlaylistTest {

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

    @Test
    void getAllPlaylistsTest() throws Exception {
        // Send two playlist to DB
        Playlist testPlaylist = new Playlist();
        testPlaylist.setPlaylistName("Test Playlist 1");
        playlistRepo.save(testPlaylist);

        Playlist testPlaylist2 = new Playlist();
        testPlaylist2.setPlaylistName("Test Playlist 2");
        playlistRepo.save(testPlaylist2);

        mockMvc.perform(get("/playlists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].playlistName", containsInAnyOrder("Test Playlist 1", "Test Playlist 2")))
                .andDo(print()); // optional: Print request & response details
    }

    @Test
    public void getPlaylistbyIdTest() throws Exception {
        // Add two playlist to DB
        Playlist testPlaylist = new Playlist();
        testPlaylist.setPlaylistName("Test Playlist 1");
        playlistRepo.save(testPlaylist);

        Playlist testPlaylist2 = new Playlist();
        testPlaylist2.setPlaylistName("Test Playlist 2");
        playlistRepo.save(testPlaylist2);

        // test playlist1 is returned (only)
        mockMvc.perform(get("/playlists/{id}","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").value(1))
                .andExpect(jsonPath("$..playlistName").value("Test Playlist 1"));
    }

    @Test
    public void addNewPlaylistAndGetTest() throws Exception {

        Map<String,String > p =  new LinkedHashMap<String, String>();
        p.put("playlistName","Test Playlist 3");

        // test add playlist2
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/playlists")
                    .content(asJsonString(p))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/playlists/{id}","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").value(1))
                .andExpect(jsonPath("$..playlistName").value("Test Playlist 3"));

        // test add playlist4
        Map<String,String > pl =  new LinkedHashMap<String, String>();
        pl.put("playlistName","Test Playlist 4");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/playlists")
                        .content(asJsonString(pl))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/playlists/{id}","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").value(2))
                .andExpect(jsonPath("$..playlistName").value("Test Playlist 4"));

        // test get all (only 2 playlists with ids 3 and 4)
        mockMvc.perform(get("/playlists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].playlistName", containsInAnyOrder("Test Playlist 3", "Test Playlist 4")))
                .andDo(print()); // optional: Print request & response details
    }

    @Test
    public void updatePlaylistTest() throws Exception {

        // Create playlist in DB
        Playlist testPlaylist = new Playlist();
        testPlaylist.setPlaylistName("Test Playlist 4");


        List<Song> songList = new ArrayList<>();
        testPlaylist.setSongs(songList);

        testPlaylist = playlistRepo.save(testPlaylist);

        // update playlist 1
        testPlaylist.setPlaylistName("Test Update Success");
        mockMvc.perform(MockMvcRequestBuilders
                    .put("/playlists/{id}",testPlaylist.getId())
                    .content(asJsonString(testPlaylist))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").value(1))
                .andExpect(jsonPath("$..playlistName").value("Test Update Success"));

    }

    @Test
    public void deletePlaylistTest() throws Exception {

        // Create playlist in DB
        Playlist p1 = new Playlist();
        p1.setPlaylistName("Test Playlist 4");
        p1 = playlistRepo.save(p1);

        // Create Artist obj to be injected into song
        Artist artist = new Artist();
        artist.setName("The Weeknd");
        artist.setSpotifyId("abc123");
        artist.setImageUrl("https://example.com/image/the-weeknd");
        artist.setGenre("Pop");
        artist = artistRepo.save(artist);

        // Put the Artist in a list
        List<Artist> artistList = new ArrayList<>();
        artistList.add(artist);

        // Create a Song object with ArtistList
        Song song = new Song();
        song.setSongName("Test Song2");
        song.setSpotifyId("777bbb");
        song.setArtists(artistList);
        song.setPreviewUrl("https://example.com/preview/");
        song.setAlbum("After Hours");

        List<Song> songList = new ArrayList<>();
        p1.setSongs(songList);
        // add songs to p1
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/playlists/{playlistId}/songs", p1.getId() )
                        .content(new ObjectMapper().writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()); // expect CREATED status 201

        // test delete playlist 1
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/playlists/{id}", 1)
                        .content(asJsonString(p1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    // helper method turn obj to json string
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}