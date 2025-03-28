package vn.iostar.embededId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CommentLikedId implements Serializable {

    @Column(name = "id_comment")
    private Long idComment;

    @Column(name = "id_user")
    private Long idUser;

}
