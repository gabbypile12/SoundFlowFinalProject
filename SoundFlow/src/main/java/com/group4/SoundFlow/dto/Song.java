package com.group4.SoundFlow.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Song {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "songid")
    private int id;

    @Column(name = "spotifyid")
    private String spotifyId;

    @Column(name="name", nullable = false)
    private String songName;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "artistsong",
            joinColumns = {@JoinColumn(name="songid")},
            inverseJoinColumns={@JoinColumn(name ="artistid")})
    private List<Artist> artists;

    // This annotation helps to prevent endless loops when retrieving json data.
    @JsonIgnore
    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists;

    @Column(name="previewurl")
    private String previewUrl;

    @Column(name="albumname")
    private String album;

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }



    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id == song.id && Objects.equals(spotifyId, song.spotifyId) && Objects.equals(songName, song.songName) && Objects.equals(artists, song.artists) && Objects.equals(playlists, song.playlists) && Objects.equals(previewUrl, song.previewUrl) && Objects.equals(album, song.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spotifyId, songName, artists, playlists, previewUrl, album);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", spotifyId='" + spotifyId + '\'' +
                ", songName='" + songName + '\'' +
                ", artists=" + artists +
                ", playlists=" + playlists +
                ", previewUrl='" + previewUrl + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
