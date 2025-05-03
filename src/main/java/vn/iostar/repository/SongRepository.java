package vn.iostar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.iostar.entity.Album;
import vn.iostar.entity.Song;
import vn.iostar.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByViewsGreaterThan(int views);

    List<Song> findByAlbum(Album album);

    List<Song> findByNameContaining(String keyword);

    Optional<Song> findById(Long id);

    Page<Song> findByOrderByViewsDesc(Pageable pageable);

    @Query("SELECT s FROM Song s LEFT JOIN SongLiked sl ON s.idSong = sl.song.idSong GROUP BY s.idSong ORDER BY COUNT(sl.song.idSong) DESC")
    Page<Song> findSongsByMostLikes(Pageable pageable);

    Page<Song> findByOrderByDayCreatedDesc(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Song s SET s.views = s.views + 1 WHERE s.idSong = :songId")
    void incrementViewCount(Long songId);

    @Override
    long count();

    @Query("SELECT s FROM Song s WHERE s.name LIKE %:query%")
    List<Song> searchSong(String query);
}
