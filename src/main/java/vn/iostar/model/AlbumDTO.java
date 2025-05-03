package vn.iostar.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iostar.entity.Album;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {

	private Long id;
    private String name;
    private String image;
    private LocalDateTime dayCreated;
    private String artistName; // <-- thêm trường này

    public AlbumDTO(Album album) {
        this.id = album.getIdAlbum();
        this.name = album.getName();
        this.image = album.getImage();
        this.dayCreated = album.getDayCreated();
        this.artistName = album.getUser() != null ? album.getUser().getNickname() : null;
    }
	

}
