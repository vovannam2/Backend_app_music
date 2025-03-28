package vn.iostar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.iostar.embededId.ArtistSongId;
import jakarta.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artist_songs")
public class ArtistSong implements Serializable {

    @Getter
    @EmbeddedId
    private ArtistSongId artistSongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_artist", referencedColumnName = "id_user", insertable = false, updatable = false)
    private User artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_song", referencedColumnName = "id_song", insertable = false, updatable = false)
    private Song song;
}
