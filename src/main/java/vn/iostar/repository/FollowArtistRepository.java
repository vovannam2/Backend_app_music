package vn.iostar.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.iostar.embededId.FollowArtistId;
import vn.iostar.entity.FollowArtist;

import java.util.List;

@Repository
public interface FollowArtistRepository extends JpaRepository<FollowArtist, FollowArtistId> {

    @Query("SELECT f.user.idUser FROM FollowArtist f WHERE f.followArtistId.idArtist = :artistId")
    List<Long> findUserIdsByArtistId(Long artistId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM FollowArtist f WHERE f.followArtistId.idArtist = :artistId AND f.followArtistId.idUser = :userId")
    boolean existsByArtistIdAndUserId(long artistId, long userId);


}