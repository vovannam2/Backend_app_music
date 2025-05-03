package vn.iostar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import vn.iostar.embededId.CommentLikedId;
import vn.iostar.entity.CommentLiked;
import vn.iostar.repository.CommentLikedRepository;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class CommentLikedService {
    private final CommentLikedRepository commentLikedRepository;
    @Autowired
    public CommentLikedService(CommentLikedRepository commentLikedRepository) {
        this.commentLikedRepository = commentLikedRepository;
    }

    @Query("SELECT COUNT(likes) FROM CommentLiked likes WHERE likes.commentLikedId.idComment = ?1 AND likes.commentLikedId.idUser = ?2")
    public Long countLikesByCommentIdAndUserId(Long id_comment, Long id_user){
        return commentLikedRepository.countLikesByCommentIdAndUserId(id_comment, id_user);
    }

    public boolean isUserLikedComment(Long id_comment, Long id_user) {
        return commentLikedRepository.isUserLikedComment(id_comment, id_user);
    }

    public <S extends CommentLiked> S save(S entity) {
        return commentLikedRepository.save(entity);
    }

    @Transactional
    public void toggleLike(Long id_comment, Long id_user) {
        if (isUserLikedComment(id_comment, id_user)) {
            commentLikedRepository.deleteByCommentLikedId(new CommentLikedId(id_comment, id_user));
            commentLikedRepository.decreaseViewCount(id_comment);
        } else {
            CommentLiked commentLikedEntity = new CommentLiked();
            commentLikedEntity.setCommentLikedId(new CommentLikedId(id_comment, id_user));
            commentLikedRepository.save(commentLikedEntity);
            commentLikedRepository.increaseLikesCount(id_comment);

        }
    }

    public int countLikesByCommentId(Long id_comment){
        return commentLikedRepository.countLikesByCommentId(id_comment);
    }

    public List<CommentLiked> findByCommentLikedId_IdComment(Long id_comment) {
        return commentLikedRepository.findByCommentLikedId_IdComment(id_comment);
    }
}
