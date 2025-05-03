package vn.iostar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.iostar.response.Response;
import vn.iostar.service.CommentLikedService;

@RestController
@RequestMapping("/api/v1")
public class CommentLikedController {

    @Autowired
    private CommentLikedService commentLikedService;

    @GetMapping("/song/isUserLikedComment")
    public ResponseEntity<?> isUserLikedComment(@RequestParam("commentId") Long commentId, @RequestParam("userId") Long userId) {
        boolean isLiked = commentLikedService.isUserLikedComment(commentId, userId);
        Response res = new Response(true, false, "Check Successfully!", isLiked);
        return ResponseEntity.ok(res);
    }

    @PostMapping("song/comment/like")
    public ResponseEntity<?> toggleLike(@RequestParam("commentId") Long commentId, @RequestParam("userId") Long userId) {
        commentLikedService.toggleLike(commentId, userId);
        boolean isLiked = commentLikedService.isUserLikedComment(commentId, userId);
        Response res = new Response(true, false, "Successfully!", isLiked);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/song/comment/countLikes")
    public ResponseEntity<?> countLikesByCommentId(@RequestParam("comment_id") Long commentId) {
        int countLikes = commentLikedService.countLikesByCommentId(commentId);
        Response res = new Response(true, false, "Successfully!", countLikes);
        return ResponseEntity.ok(res);
    }
}

