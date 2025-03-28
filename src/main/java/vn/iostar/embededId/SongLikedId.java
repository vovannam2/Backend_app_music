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
public class SongLikedId implements Serializable {

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "id_song")
    private Long idSong;
}
