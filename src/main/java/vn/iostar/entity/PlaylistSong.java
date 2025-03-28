package vn.iostar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iostar.embededId.PlaylistSongId;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlist_songs")
public class PlaylistSong implements Serializable {

    @EmbeddedId
    private PlaylistSongId playlistSongId;

    @Column(name = "day_added")
    private LocalDateTime dayAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_playlist", referencedColumnName = "id_playlist", insertable = false,  updatable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_song", referencedColumnName = "id_song", insertable = false,  updatable = false)
    private Song song;
}
