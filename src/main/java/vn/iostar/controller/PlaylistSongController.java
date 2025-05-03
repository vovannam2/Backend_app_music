package vn.iostar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.iostar.response.Response;
import vn.iostar.model.SongModel;
import vn.iostar.service.PlaylistSongService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlistSong")
public class PlaylistSongController {
    @Autowired
    private PlaylistSongService playlistSongService;

    @GetMapping("/{id_playlist}/songs")
    public ResponseEntity<?> findAllSongByPlaylistId(@PathVariable Long id_playlist) {
        List<SongModel> songs = playlistSongService.findAllByPlaylistSongId(id_playlist);
        Response res = new Response(true, false, "Get Songs Of Playlist Successfully!", songs);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id_playlist}/{id_song}")
    public ResponseEntity<?> deleteSongFromPlaylist(@PathVariable Long id_playlist, @PathVariable Long id_song) {
        int isSuccess = playlistSongService.deleteByPlaylistSongId(id_playlist, id_song);
        int countSongs = playlistSongService.countSongsByPlaylistId(id_playlist);
        Response res = new Response(true, false, "Delete Song From Playlist Successfully!", countSongs);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{id_playlist}/{id_song}")
    public ResponseEntity<?> addSongToPlaylist(@PathVariable Long id_playlist, @PathVariable Long id_song) {
        boolean songIsExists = playlistSongService.isSongExistsInPlaylist(id_playlist, id_song);
        if (!songIsExists) {
            playlistSongService.addSongToPlaylist(id_playlist, id_song);
        }
        Response res = new Response(true, false, "Success", !songIsExists);
        return ResponseEntity.ok(res);
    }
}
