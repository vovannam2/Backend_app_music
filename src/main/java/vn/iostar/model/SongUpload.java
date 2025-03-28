package vn.iostar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongUpload {
//    private LocalDateTime dayCreated;
    private MultipartFile imageFile;
    private String name;
    private MultipartFile resourceFile;
    private Long idAlbum;
    private Long idSongCategory;
}
