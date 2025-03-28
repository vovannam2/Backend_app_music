package vn.iostar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iostar.entity.Song;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumModel {
    private Long idAlbum;
    private String name;
    private String image;
    private int countSong;
    private List<Song> songs;
}
