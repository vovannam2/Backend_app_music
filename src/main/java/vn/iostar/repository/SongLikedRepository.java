package vn.iostar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.iostar.embededId.SongLikedId;
import vn.iostar.entity.Song;
import vn.iostar.entity.SongLiked;

import java.util.List;

@Repository
public interface SongLikedRepository extends JpaRepository<SongLiked, Long> {
    @Query("SELECT COUNT(s) FROM SongLiked s WHERE s.songLikedId.idSong = ?1")
    Long countLikesBySongId(Long songId);

    @Query("SELECT COUNT(s) FROM SongLiked s WHERE s.songLikedId.idSong = ?1 AND s.songLikedId.idUser = ?2")
    Long countLikesBySongIdAndUserId(Long songId, Long userId);

    default boolean isUserLikedSong(Long songId, Long userId) {
        return countLikesBySongIdAndUserId(songId, userId) > 0;
    }

    @Query("SELECT s.song FROM SongLiked s WHERE s.songLikedId.idUser = ?1 ORDER BY s.dayLiked DESC")
    List<Song> getLikedSongsByIdUser(Long idUser);

    @Transactional
    @Modifying
    @Query("DELETE FROM SongLiked s WHERE s.songLikedId = ?1")
    void deleteBySongLikedId(SongLikedId songLikedId);
    @Query("SELECT s FROM Song s WHERE s.idSong NOT IN (SELECT s.songLikedId.idSong FROM SongLiked s WHERE s.songLikedId.idUser = ?1)")

    List<Song> getNotLikedSongsByIdUser(Long idUser);
}
