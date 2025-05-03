package vn.iostar.controller;

import jdk.jfr.Category;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import vn.iostar.response.Response;
import vn.iostar.embededId.ArtistSongId;
import vn.iostar.entity.*;
import vn.iostar.model.ResponseMessage;
import vn.iostar.model.SongModel;
import vn.iostar.repository.SongRepository;
import vn.iostar.repository.UserRepository;
import vn.iostar.service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class SongController {
    @Autowired
    private SongService songService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ArtistSongService artistSongService;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongCategoryService songCategoryService;
    @Autowired
    private UserService userService;


    @GetMapping("/songs")
    public ResponseEntity<?> getAllSongs() {
        List<SongModel> songModelss = songService.getAllSongs();
        Response res = new Response(true, false, "Get Songs Successfully!", songModelss);
//        List<Song> songs = songService.getAllSongs();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/songs/count")
    public ResponseEntity<?> getSongsCount() {
        long cnt = songService.countSongs();
        Response res = new Response(true, false, "Get Songs Successfully!", cnt);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/song/{id}")
    public ResponseEntity<?> getSongById(@PathVariable("id") Long id) {
        Optional<Song> optSong = songService.getSongById(id);
        if(optSong.isPresent()) {
            SongModel songModel = new SongModel();
            BeanUtils.copyProperties(optSong.get(), songModel);
            songModel.setCntComments(optSong.get().getSongComments().size());
            songModel.setCntLikes(optSong.get().getSongLikeds().size());
            songModel.setArtistId(optSong.get().getArtistSongs().get(0).getArtistSongId().getIdArtist());
            songModel.setArtistName(userRepository.getById(optSong.get().getArtistSongs().get(0).getArtistSongId().getIdArtist()).getNickname());
            Response res = new Response(true, true, "Get Song Successfully!", songModel);
            return ResponseEntity.ok(res);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Do not find song");
    }

    @DeleteMapping("/song/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable("id") Long id) {
        Optional<Song> optSong = songService.getSongById(id);
        if(optSong.isPresent()) {
            Song song = optSong.get();
            songRepository.delete(song);
            ResponseMessage responseMessage = new ResponseMessage("Delete Successfully!", true, false);
            return ResponseEntity.ok(responseMessage);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Do not find song");
    }

    @GetMapping("/songs/search")
    public ResponseEntity<?> getSongsByKeyword(@RequestParam("query") String query) {
        List<SongModel> songs = songService.getSongsByKeyWord(query);
        Response res = new Response(true, false, "Find Songs Successfully!", songs);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/song/{id}/view")
    public ResponseEntity<?> increaseViewOfSong(@PathVariable("id") Long id) {
        Optional<Song> optSong = songService.getSongById(id);
        if(optSong.isPresent()) {
            songService.increaseViewOfSong(id);
            Response res = new Response(true, false, "Successfully!", songService.getSongById(id).get());
            return ResponseEntity.ok(res);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Do not find song");
    }

    @GetMapping("/song/{songId}/artists")
    public ResponseEntity<?> getArtistBySongId(@PathVariable Long songId) {
        List<User> artists = artistSongService.findArtistBySongId(songId);
        Response res = new Response(true, false, "Get Artists Successfully!", artists);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/song/most-views")
    public ResponseEntity<?> getSongsByMostViews(Pageable pageable) {
        Page<SongModel> songs = songService.getSongsByMostViews(pageable);
        Response res = new Response(true, false, "Get Songs By Most Views Successfully!", songs);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/song/most-likes")
    public ResponseEntity<?> getSongsByMostLikes(Pageable pageable) {
        Page<SongModel> songs = songService.getSongsByMostLikes(pageable);
        Response res = new Response(true, false, "Get Songs By Most Likes Successfully!", songs);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/song/new-released")
    public ResponseEntity<?> getSongsByDayCreated(Pageable pageable) {
        Page<SongModel> songs = songService.getSongsByDayCreated(pageable);
        Response res = new Response(true, false, "Get Songs By Day Created Successfully!", songs);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/upload-song")
    public ResponseEntity<?> uploadSong(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("idArtist") Long idArtist,
            @RequestParam("name") String name,
            @RequestParam("idSongCategory") Long idSongCategory,
            @RequestParam("idAlbum") Long idAlbum,
            @RequestParam("resourceFile") MultipartFile resourceFile) throws IOException {
        String resource = songService.uploadAudio(resourceFile);
        name = name.replace("\"", "");
        Song song = new Song();
        song.setName(name);
        if(imageFile != null) {
            String image = imageService.uploadImage(imageFile);
            song.setImage(image);
        } else {
            song.setImage("https://res.cloudinary.com/dv3gj6qre/image/upload/v1716148451/evdts4vwt5bqfcn2seak.jpg");
        }
        song.setResource(resource);
        song.setDayCreated(LocalDateTime.now());
        Optional<Album> album = albumService.getAlbumById(idAlbum);
        if(album.isPresent()) {
            song.setAlbum(album.get());
        }
        Optional<SongCategory> category = songCategoryService.findById(idSongCategory);
        if(category.isPresent()) {
            song.setSongCategory(category.get());
        }
        Song newSong = songRepository.save(song);
        User artist = userService.findByIdUser(idArtist).get();
        ArtistSongId artistSongId = new ArtistSongId(artist.getIdUser(), newSong.getIdSong());
        artistSongService.save(new ArtistSong(artistSongId, artist, newSong));
        SongModel songModel = new SongModel();
        BeanUtils.copyProperties(song, songModel);
        songModel.setArtistId(idArtist);
        songModel.setArtistName(artist.getNickname());
        Response res = new Response(true, false, "Uploaded Successfully!", songModel);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/song/update")
    public ResponseEntity<?> updateSong(@RequestPart("idSong") Long idSong, @RequestPart("imageFile") @Nullable MultipartFile imageFile, @RequestPart("songName") String songName) throws IOException {
        Optional<Song> optFoundSong = songService.getSongById(idSong);
        if(optFoundSong.isPresent()) {
            Song foundSong = optFoundSong.get();
            if(imageFile != null) {
                String imageUrl = imageService.uploadImage(imageFile);
                foundSong.setImage(imageUrl);
            }
            foundSong.setName(songName.replace("\"", ""));
            songService.saveSong(foundSong);
            return ResponseEntity.ok(new ResponseMessage("Update Song Successfully!", true, false));
        }
        return ResponseEntity.ok(new ResponseMessage("Update Song Fail!", false, true));
    }
}
