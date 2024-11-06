package com.group4.SoundFlow.dao;

import com.group4.SoundFlow.dto.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface SongRepo extends JpaRepository<Song, Integer> {
    // fetch the Song associated with a specific Playlist based on the provided IDs
    @Query("SELECT s FROM Song s JOIN s.playlists p WHERE p.id = ?1 AND s.id = ?2")
    Optional<Song> findByPlaylistIdAndSongId(int playlistId, int songId);

    Optional<Song> findBySpotifyId(String spotifyId);

    // Reset auto increment in table
    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE song AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
