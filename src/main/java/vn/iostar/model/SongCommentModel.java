package vn.iostar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iostar.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongCommentModel {
    private Long idComment;
    private String content;
    private int likes;
    private User user;
    private LocalDateTime dayCommented;
    private List<Long> listUserLike;
}
