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
public class PlaylistSongId implements Serializable {
    @Column(name = "id_playlist")
    private Long idPlaylist;

    @Column(name = "id_song")
    private Long idSong;
}
