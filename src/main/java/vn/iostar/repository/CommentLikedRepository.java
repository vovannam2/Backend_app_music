package vn.iostar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.iostar.embededId.CommentLikedId;
import vn.iostar.entity.CommentLiked;
import vn.iostar.entity.User;

import java.util.List;

@Repository
public interface CommentLikedRepository extends JpaRepository<CommentLiked, CommentLikedId> {
    @Query("SELECT cmt.user FROM CommentLiked cmt WHERE cmt.commentLikedId.idComment = ?1")
    List<User> findAllUsersByComment(Long id_comment);

    @Query("SELECT COUNT(likes) FROM CommentLiked likes WHERE likes.commentLikedId.idComment = ?1 AND likes.commentLikedId.idUser = ?2")
    Long countLikesByCommentIdAndUserId(Long id_comment, Long id_user);

    @Query("SELECT COUNT(likes) FROM CommentLiked likes WHERE likes.commentLikedId.idComment = ?1")
    int countLikesByCommentId(Long id_comment);

    default boolean isUserLikedComment(Long id_comment, Long id_user) {
        return countLikesByCommentIdAndUserId(id_comment, id_user) > 0;
    }

    void deleteByCommentLikedId(CommentLikedId commentLikedId);

    @Transactional
    @Modifying
    @Query("UPDATE SongComment cmt SET cmt.likes = cmt.likes + 1 WHERE cmt.idComment = :commentId")
    void increaseLikesCount(Long commentId);

    @Transactional
    @Modifying
    @Query("UPDATE SongComment cmt SET cmt.likes = cmt.likes + 1 WHERE cmt.idComment = :commentId")
    void decreaseViewCount(Long commentId);

    List<CommentLiked> findByCommentLikedId_IdComment(Long id_comment);
}
