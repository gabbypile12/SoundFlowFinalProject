package com.group4.SoundFlow.dao;

import com.group4.SoundFlow.dto.Artist;
import com.group4.SoundFlow.dto.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ArtistRepo extends JpaRepository<Artist, Integer> {
    Optional<Artist> findBySpotifyId(String spotifyId);

    // Count songs for the artist excluding the specified song
    @Query("SELECT COUNT(s) FROM Song s JOIN s.artists a WHERE a.id = ?1 AND s.id <> ?2")
    int countBySongsContainingExcludingSong(int artistId, Integer songId);

    // Reset auto increment in table
    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE artist AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

}
