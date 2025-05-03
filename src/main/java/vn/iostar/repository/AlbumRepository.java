package vn.iostar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.iostar.entity.Album;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("select a from Album a where a.user.idUser = :idArtist")
    List<Album> getAlbumByIdArtist(@Param("idArtist") Long idArtist);

    @Query("SELECT COUNT(a) FROM Album a WHERE a.user.idUser = :idArtist")
    int countAlbumsByArtistId(@Param("idArtist") Long idArtist);

    List<Album> findByNameContaining(String name);
}
