package com.group4.SoundFlow.dao;

import com.group4.SoundFlow.dto.Playlist;
import com.group4.SoundFlow.dto.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PlaylistRepo extends JpaRepository<Playlist, Integer> {
    // Count for specific song in playlists
    int countBySongsContaining(Song song);

    // Reset auto increment in table
    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE playlist AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
