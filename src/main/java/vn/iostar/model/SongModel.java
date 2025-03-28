package vn.iostar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongModel {
    private Long idSong;
    private String name;
    private int views;
    private LocalDateTime dayCreated;
    private String resource;
    private String image;
    private Long artistId;
    private String artistName;
    private int cntComments;
    private int cntLikes;
}
