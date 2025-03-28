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
@Table(name = "songs")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_song")
    private Long idSong;

    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(1000)")
    private String name;

    @Column(name = "views")
    private int views;

    @Column(name = "day_created", nullable = false)
    private LocalDateTime dayCreated;

    @Column(name = "resource", nullable = false, columnDefinition = "varchar(1000)")
    private String resource;

    @Column(name = "image", nullable = false, columnDefinition = "varchar(1000)")
    private String image;

    @JsonIgnoreProperties("songs")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_album", referencedColumnName = "id_album")
    private Album album;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL)
    private List<PlaylistSong> playlistSongs;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL)
    private List<SongLiked> songLikeds;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL)
    private List<ArtistSong> artistSongs;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL)
    private List<SongComment> songComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "id_song_category", referencedColumnName = "id_song_category")
    private SongCategory songCategory;

    public Song(Long idSong) {
        this.idSong = idSong;
    }
}
