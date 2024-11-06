package com.group4.SoundFlow.dto;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity//(name = "Playlist")
public class Playlist {

    @Id
    @Column(name = "playlistid")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "playlistsong",
            joinColumns = {@JoinColumn(name = "playlistid")},
            inverseJoinColumns = {@JoinColumn(name = "songid")})
    private List<Song> songs;

    @Column(name="name", nullable = false)
    private String playlistName;


    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
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
        Playlist playlist = (Playlist) o;
        return id == playlist.id && Objects.equals(songs, playlist.songs) && Objects.equals(playlistName, playlist.playlistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, songs, playlistName);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", songs=" + songs +
                ", playlistName='" + playlistName + '\'' +
                '}';
    }
}
