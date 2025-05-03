package vn.iostar.controller;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import vn.iostar.entity.Album;
import vn.iostar.entity.Song;
import vn.iostar.model.AlbumDTO;
import vn.iostar.model.AlbumModel;
import vn.iostar.model.ResponseMessage;
import vn.iostar.model.SongModel;
import vn.iostar.response.Response;
import vn.iostar.service.AlbumService;
import vn.iostar.service.ImageService;
import vn.iostar.service.SongService;
import vn.iostar.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/albums")
    public ResponseEntity<?> getAllAlbums() {
        List<Album> albums = albumService.getAllAlbums();
        List<AlbumDTO> albumDTOs = albums.stream()
                .map(AlbumDTO::new)
                .collect(Collectors.toList());
        
        Response res = new Response(true, false, "Get Albums Successfully!", albumDTOs);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/album/{id}")
    public ResponseEntity<?> getAlbumById(@PathVariable("id") Long id) {
        Optional<Album> foundAlbum = albumService.getAlbumById(id);
        if (foundAlbum.isPresent()) {
            AlbumModel model = new AlbumModel();
            BeanUtils.copyProperties(foundAlbum.get(), model);
            model.setCountSong(foundAlbum.get().getSongs().size());
            Response res = new Response(true, false, "Get Album Successfully!", model);
            return ResponseEntity.ok(res);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find album with id: " + id);
    }

    @PatchMapping("/album/update")
    public ResponseEntity<?> updateAlbum(@RequestPart("idAlbum") Long idAlbum, @RequestPart("imageFile") @Nullable MultipartFile imageFile, @RequestPart("albumName") String albumName) throws IOException {
        Optional<Album> foundAlbum = albumService.getAlbumById(idAlbum);
        if(foundAlbum.isPresent()) {
            Album album = foundAlbum.get();
            if(imageFile != null) {
                String url = imageService.uploadImage(imageFile);
                album.setImage(url);
            }
            album.setName(albumName.replace("\"", ""));
            albumService.saveAlbum(album);
            return ResponseEntity.ok(new ResponseMessage("Update Album Successfully!", true, false));
        }
        return ResponseEntity.ok(new ResponseMessage("Update Album Fail!", false, true));
    }

    @DeleteMapping("/album/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable("id") Long id) {
        Optional<Album> foundAlbum = albumService.getAlbumById(id);
        if (foundAlbum.isPresent()) {
            albumService.deleteAlbum(foundAlbum.get());
            ResponseMessage responseMessage = new ResponseMessage("Delete Successfully!", true, false);
            return ResponseEntity.ok(responseMessage);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find album with id: " + id);
    }

    @GetMapping("/album/{id}/songs")
    public ResponseEntity<?> getSongByAlbumId(@PathVariable("id") Long id) {
        Optional<Album> optAlbum = albumService.getAlbumById(id);
        if(optAlbum.isPresent()) {
            List<Song> songs = songService.getSongByAlbum(optAlbum.get());
            List<SongModel> songModels = new ArrayList<>();
            for(Song song : songs) {
                SongModel songModel = new SongModel();
                BeanUtils.copyProperties(song, songModel);
                songModel.setCntComments(song.getSongComments().size());
                songModel.setCntLikes(song.getSongLikeds().size());
                songModels.add(songModel);
            }
            Response res = new Response(true, false, "Get Songs Of Album Successfully!", songModels);
            return ResponseEntity.ok(res);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Do not find album");
    }

    @GetMapping("/albums/artist/{idArtist}")
    public ResponseEntity<?> getAlbumByArtistId(@PathVariable("idArtist") Long idArtist) {
        List<Album> albums = albumService.getAlbumByIdArtist(idArtist);
        List<AlbumModel> albumModelList = new ArrayList<>();
        if (!albums.isEmpty()) {
            for (Album album : albums) {
                AlbumModel model = new AlbumModel();
                BeanUtils.copyProperties(album, model);
                albumModelList.add(model);
            }
        }
        Response res = new Response(true, false, "Get Albums Successfully!", albumModelList);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/albums/search")
    public ResponseEntity<?> getAlbumByKeyword(@RequestParam("name") String keyword) {
        List<Album> foundAlbums = albumService.getAlbumByKeyword(keyword);
        if (foundAlbums.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No result");
        }
        Response res = new Response(true, false, "Successfully!", foundAlbums);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/album/upload")
    public ResponseEntity<?> uploadAlbum(@RequestPart("image") @Nullable MultipartFile image, @RequestPart("idArtist") Long idArtist, @RequestPart("albumName") String albumName, @RequestPart("listSong") String listSong) throws IOException {
        Album album = new Album();
        albumName = albumName.replace("\"", "");
        album.setName(albumName);
        album.setUser(userService.getUserById(idArtist).get());
        if(image != null) {
            String imageUrl = imageService.uploadImage(image);
            album.setImage(imageUrl);
        } else {
            album.setImage("https://res.cloudinary.com/dv3gj6qre/image/upload/v1716148451/evdts4vwt5bqfcn2seak.jpg");
        }
        album.setDayCreated(LocalDateTime.now());
        Album savedAlbum = albumService.saveAlbum(album);

        System.out.println(listSong);
        listSong = listSong.substring(1, listSong.length() - 1);
        String[] nums = listSong.split(",");
        List<Long> selectedSongs = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
            selectedSongs.add(Long.parseLong(nums[i]));
        }


        for(Long songId : selectedSongs) {
            Optional<Song> foundSong = songService.getSongById(songId);
            if(foundSong.isPresent()) {
                Song song = foundSong.get();
                song.setAlbum(savedAlbum);
                songService.saveSong(song);
            }
        }

        ResponseMessage res = new ResponseMessage();
        res.setMessage("Upload Album Successfully!");
        res.setError(false);
        res.setSuccess(true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/albums/artist/{idArtist}/count")
    public ResponseEntity<?> getCountOfAlbums(@PathVariable("idArtist") Long idArtist) {
        Response res = new Response();
        res.setSuccess(true);
        res.setError(false);
        res.setMessage("Get Quantity Successfully!");
        res.setData(albumService.countAlbumsByArtistId(idArtist));
        return ResponseEntity.ok(res);
    }


}
