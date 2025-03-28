package vn.iostar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "song_comments")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class SongComment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Long idComment;

    @Column(name = "content", nullable = false, columnDefinition = "nvarchar(1000)")
    private String content;

    @Column(name = "likes", nullable = false)
    private int likes;

    @Column(name = "day_commented")
    private LocalDateTime dayCommented;

    @OneToMany(mappedBy = "songComment", cascade = CascadeType.ALL)
    private List<CommentLiked> commentLikeds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_song", referencedColumnName = "id_song")
    private Song song;
}
