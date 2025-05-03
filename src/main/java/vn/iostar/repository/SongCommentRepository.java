package vn.iostar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iostar.entity.SongComment;
import vn.iostar.entity.Song;
import vn.iostar.entity.User;


import java.util.List;
import java.util.Optional;

@Repository
public interface SongCommentRepository extends JpaRepository<SongComment, Long> {

    List<SongComment> findAllCommentsBySong(Optional<Song> song);

    List<SongComment> findAllComentsByUser(Optional<User> user);


    @Override
    <S extends SongComment> S save(S entity);

    Optional<SongComment> findCommentByIdComment(Long id_comment);



}
