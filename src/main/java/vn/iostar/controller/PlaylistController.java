package vn.iostar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.iostar.response.Response;
import vn.iostar.service.PlaylistService;
import vn.iostar.entity.Playlist;
import vn.iostar.model.PlaylistModel;
import vn.iostar.model.PlaylistRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @PostMapping("/playlist")
    public ResponseEntity<?> createPlaylist(@RequestBody PlaylistRequest requestBody) {
        PlaylistModel playlist = playlistService.createPlaylist(requestBody);
        Response response = new Response(true, false,"Playlist deleted", playlist);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/playlist/{id_playlist}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id_playlist) {
        Playlist playlist = playlistService.getPlaylistById(id_playlist).orElseThrow();
        playlistService.deletePlaylist(playlist);
        Response response = new Response(true, false,"Playlist deleted", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/playlist/{id_playlist}")
    public ResponseEntity<?> getPlaylistById(@PathVariable Long id_playlist) {
        Playlist playlist = playlistService.getPlaylistById(id_playlist).orElseThrow();
        playlistService.setPlaylistImageByFirstSongImage(id_playlist);
        playlistService.sortPlaylistSongsByDayAdded(playlist);
        PlaylistModel playlistModel = playlistService.convertToPlaylistModel(playlist);
        Response response = new Response(true, false,"Playlist found", playlistModel);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/playlist")
    public ResponseEntity<?> isPlaylistNameExists(@RequestParam String name) {
        boolean isExists = playlistService.isPlaylistNameExists(name);
        Response response = new Response(true, false,"Successfully!", isExists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/playlist/{id_playlist}")
    public ResponseEntity<?> updatePlaylistNameById(@PathVariable Long id_playlist, @RequestParam String name) {
        Optional<Playlist> playlist = playlistService.getPlaylistById(id_playlist);
        if (playlist.isPresent()) {
            playlist.get().setName(name);
            boolean isExists = playlistService.isPlaylistNameExists(playlist.get().getName());
            if (!isExists) {
                playlistService.savePlaylist(playlist.get());
            } else {
                Response response = new Response(true, false, "Playlist name already exists", false);
                return ResponseEntity.ok(response);
            }
        }
        Response response = new Response(true, false, "Playlist updated", playlist.isPresent());
        return ResponseEntity.ok(response);
    }

}
