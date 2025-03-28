package vn.iostar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistModel {
    private Long idPlaylist;
    private Long idUser;
    private String name;
    private LocalDateTime dayCreated;
    private String image;
    List<SongModel> songs;

}
