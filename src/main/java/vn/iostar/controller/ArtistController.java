package vn.iostar.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import vn.iostar.response.Response;
import vn.iostar.entity.Album;
import vn.iostar.entity.Role;
import vn.iostar.entity.Song;
import vn.iostar.entity.User;
import vn.iostar.model.AlbumModel;
import vn.iostar.service.AlbumService;
import vn.iostar.service.ArtistSongService;
import vn.iostar.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {
    @Autowired
    UserService userService;

    @Autowired
    AlbumService albumService;

    @Autowired
    ArtistSongService artistSongService;

    @GetMapping("/artists")
    public ResponseEntity<?> getAllArtists(Pageable pageable) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ARTIST);
        Page<User> artists = userService.findByRoles(roles, pageable);
        Response res = new Response(true, false, "Get Songs By Most Likes Successfully!", artists);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<?> getArtistById(@PathVariable("id") Long id) {
        Optional<User> foundArtist = userService.findByIdUser(id);
        if (foundArtist.isPresent()) {
            Response res = new Response(true, false, "Get Artist Successfully!", foundArtist.get());
            return ResponseEntity.ok(res);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find artist with id: " + id);
    }

    @GetMapping("/artist/{id}/albums")
    public ResponseEntity<?> getAlbumByIdArtist(@PathVariable("id") Long idArtist) {
        Optional<User> foundArtist = userService.findByIdUser(idArtist);
        if (foundArtist.isPresent()) {
            List<Album> albums = albumService.getAlbumByIdArtist(idArtist);
            List<AlbumModel> albumModels = new ArrayList<>();
            for (Album album : albums) {
                AlbumModel albumModel = new AlbumModel();
                BeanUtils.copyProperties(album, albumModel);
                albumModel.setCountSong(album.getSongs().size());
                albumModels.add(albumModel);
            }
            Response res = new Response(true, false, "Get Albums Of Artist Successfully!", albumModels);
            return ResponseEntity.ok(res);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find albums of this artist");
    }

    @GetMapping("/artists/search")
    public ResponseEntity<?> getArtistByKeyword (@RequestParam("name") String keyword) {
        List<User> foundArtists = userService.getArtistByKeyword(keyword);
        if (foundArtists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No result");
        }
        Response res = new Response(true, false, "Search Successfully!", foundArtists);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/artist/{idArtist}/songs/desc")
    public ResponseEntity<?> getSongsOfArtistDesc(@PathVariable("idArtist") Long idArtist) {
        List<Song> songs = artistSongService.getSongsOfArtistDesc(idArtist);
        Response response = new Response();
        response.setData(songs);
        response.setMessage("Get Songs Of Artist Successfully!");
        response.setError(false);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/artist/{idArtist}/followers")
    public ResponseEntity<?> getAllFollowers(@PathVariable("idArtist") Long idArtist){
        List<Long> ids = userService.getAllFollowers(idArtist);
        Response res = new Response();
        res.setError(false);
        res.setSuccess(true);
        res.setMessage("Get All Users Successfully!");
        res.setData(ids);
        return ResponseEntity.ok(res);
    }

}
