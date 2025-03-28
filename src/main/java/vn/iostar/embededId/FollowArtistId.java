package vn.iostar.embededId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FollowArtistId implements Serializable {
    @Column(name = "id_artist")
    private Long idArtist;

    @Column(name = "id_user")
    private Long idUser;
}
