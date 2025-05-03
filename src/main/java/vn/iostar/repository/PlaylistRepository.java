package vn.iostar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.iostar.entity.Playlist;
import vn.iostar.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser(User user);

    @Query("SELECT p FROM Playlist p WHERE p.user.idUser = ?1 ORDER BY p.dayCreated DESC")
    List<Playlist> getPlaylistsByIdUser(Long idUser);

    @Query("SELECT p FROM Playlist p WHERE p.name = ?1")
    Optional<Playlist> findPlaylistByName(String name);

}
