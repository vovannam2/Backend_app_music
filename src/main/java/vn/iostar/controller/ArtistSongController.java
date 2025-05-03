package vn.iostar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iostar.response.Response;
import vn.iostar.model.SongModel;
import vn.iostar.service.ArtistSongService;


@RestController
@RequestMapping("/api/v1")
public class ArtistSongController {
    @Autowired
    private ArtistSongService artistSongService;

    // Get Songs by ArtistId
    @GetMapping("/artist/{artistId}/songs")
    public ResponseEntity<?> getAllSongsByArtistId(@PathVariable Long artistId, Pageable pageable) {
        Page<SongModel> songs = artistSongService.findAllSongsByArtistId(artistId, pageable);
        Response res = new Response(true, false, "Get Songs Of Artist Successfully!", songs);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/artist/{idArtist}/songs/count")
    public ResponseEntity<?> getCountOfSongs(@PathVariable("idArtist") Long idArtist) {
        int count = artistSongService.countSongsByArtistId(idArtist);
        Response res = new Response(true, false, "Get Count Of Songs Successfully!", count);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/artist/{idArtist}/views/count")
    public ResponseEntity<?> getCountOfViews(@PathVariable("idArtist") Long idArtist) {
        Response res = new Response();
        res.setData(artistSongService.countTotalViewsByArtistId(idArtist));
        res.setMessage("Get Count Songs Of Artist Successfully!");
        res.setError(false);
        res.setSuccess(true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/artist/{idArtist}/likes/count")
    public ResponseEntity<?> getCountOfLikes(@PathVariable("idArtist") Long idArtist) {
        Response res = new Response();
        res.setData(artistSongService.countUsersByArtistId(idArtist));
        res.setMessage("Get Count Songs Of Artist Successfully!");
        res.setError(false);
        res.setSuccess(true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/artist/{idArtist}/comments/count")
    public ResponseEntity<?> getCountOfComments(@PathVariable("idArtist") Long idArtist) {
        Response res = new Response();
        res.setData(artistSongService.countCommentsByArtistId(idArtist));
        res.setMessage("Get Count Songs Of Artist Successfully!");
        res.setError(false);
        res.setSuccess(true);
        return ResponseEntity.ok(res);
    }
}
