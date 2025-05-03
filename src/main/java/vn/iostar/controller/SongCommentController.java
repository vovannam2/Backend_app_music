package vn.iostar.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import vn.iostar.response.Response;
import vn.iostar.entity.CommentLiked;
import vn.iostar.entity.SongComment;
import vn.iostar.entity.Song;
import vn.iostar.entity.User;
import vn.iostar.model.ResponseMessage;
import vn.iostar.model.SongCommentModel;
import vn.iostar.model.SongCommentRequest;
import vn.iostar.service.CommentLikedService;
import vn.iostar.service.SongCommentService;
import vn.iostar.service.SongService;
import vn.iostar.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class SongCommentController {
    @Autowired
    private SongCommentService songCommentService;

    @Autowired
    private CommentLikedService commentLikedService;

    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    // Get Comments by Song
    @GetMapping("/song/{id_song}/comments")
    public ResponseEntity<?> getCommentsBySongId(@PathVariable Long id_song) {
        Optional<Song> song = songService.getSongbyId(id_song);
        List<SongComment> comments = songCommentService.findAllComentsBySong(song);
        List<SongCommentModel> commentModels = new ArrayList<>();
        for (SongComment comment : comments) {
            List<CommentLiked> commentLikeds = commentLikedService.findByCommentLikedId_IdComment(comment.getIdComment());
            List<Long> listUserLike = new ArrayList<>();
            for (CommentLiked cmtliked : commentLikeds) {
                listUserLike.add(cmtliked.getUser().getIdUser());
            }
            comment.setLikes(commentLikedService.countLikesByCommentId(comment.getIdComment().longValue()));
            SongCommentModel commentModel = new SongCommentModel();
            BeanUtils.copyProperties(comment, commentModel);
            commentModel.setUser(comment.getUser());
            commentModel.setListUserLike(listUserLike);
            commentModels.add(commentModel);
        }
        Response res = new Response(true, false, "Get Comments Of Song Successfully!", commentModels);
        return ResponseEntity.ok(res);
    }

    // Save Comment
    @PostMapping("/song/{id_song}/comments")
    public ResponseEntity<?> createComment(@RequestBody SongComment newComment) {
        LocalDateTime commentDay = LocalDateTime.now();
        newComment.setDayCommented(commentDay);
        SongComment savedComment = songCommentService.saveComment(newComment);
        Response res = new Response(true, false, "Add Comment Successfully!", newComment);
        return ResponseEntity.ok(res);
    }

    // Get likes
    @GetMapping("/song/comment/{id_comment}/likes")
    public ResponseEntity<?> getLikesOfComment(@PathVariable Long id_comment){
        Optional<SongComment> comment = songCommentService.findCommentByIdComment(id_comment);
        if(comment.isPresent()) {
            Response res = new Response(true, false, "Get Likes Of Comment Successfully!",  comment.get().getLikes());
            return ResponseEntity.ok(res);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find comment with id: " + id_comment);
    }

    @PostMapping("/song/post-comment")
    public ResponseEntity<?> postComment(@RequestBody SongCommentRequest newComment) {
        SongComment songComment = new SongComment();
        Optional<User> user = userService.getUserById(newComment.getIdUser());
        if(user.isPresent()) {
            songComment.setUser(user.get());
        }
        Optional<Song> song = songService.getSongbyId(newComment.getIdSong());
        if(song.isPresent()) {
            songComment.setSong(song.get());
        }
        songComment.setLikes(0);
        songComment.setContent(newComment.getContent());
        songComment.setDayCommented(LocalDateTime.now());
        songCommentService.saveComment(songComment);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Post Comment Successfully");
        responseMessage.setSuccess(true);
        responseMessage.setError(false);
        return ResponseEntity.ok(responseMessage);
    }
}
