package com.group4.SoundFlow.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Artist {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "artistid")
    private int id;

    @Column(name="fullname", nullable = false)
    private String name;

    @Column(name = "spotifyid")
    private String spotifyId;

    @Column(name = "imageurl")
    private String imageUrl;

    // This annotation helps to prevent endless loops when retrieving json data.
    @JsonIgnore
    @ManyToMany(mappedBy = "artists")
    private List<Song> songs;

    @Column
    private String genre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id && Objects.equals(name, artist.name) && Objects.equals(spotifyId, artist.spotifyId) && Objects.equals(imageUrl, artist.imageUrl) && Objects.equals(songs, artist.songs) && Objects.equals(genre, artist.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, spotifyId, imageUrl, songs, genre);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", spotifyId='" + spotifyId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", songs=" + songs +
                ", genre='" + genre + '\'' +
                '}';
    }
}
