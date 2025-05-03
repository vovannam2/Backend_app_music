package vn.iostar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.iostar.embededId.PlaylistSongId;
import vn.iostar.entity.PlaylistSong;
import vn.iostar.entity.Song;
import vn.iostar.model.SongModel;
import vn.iostar.repository.PlaylistSongRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlaylistSongService {

    private final PlaylistSongRepository playlistSongRepository;
    private final SongService songService;

    @Autowired
    public PlaylistSongService(PlaylistSongRepository playlistSongRepository, SongService songService) {
        this.playlistSongRepository = playlistSongRepository;
        this.songService = songService;
    }

    public List<SongModel> findAllByPlaylistSongId(Long id_playlist) {
        List<Song> songs = playlistSongRepository.findAllByPlaylistSongId(id_playlist);
        return songService.convertToSongModelList(songs);
    }

    public int deleteByPlaylistSongId(Long id_playlist, Long id_song) {
        return playlistSongRepository.deleteByPlaylistSongId(id_playlist, id_song);
    }

    public List<PlaylistSong> findAll() {
        return playlistSongRepository.findAll();
    }

    public List<PlaylistSong> findAllById(Iterable<Long> longs) {
        return playlistSongRepository.findAllById(longs);
    }

    public <S extends PlaylistSong> S save(S entity) {
        return playlistSongRepository.save(entity);
    }

    public void delete(PlaylistSong entity) {
        playlistSongRepository.delete(entity);
    }

    public void deleteAllById(Iterable<? extends Long> longs) {
        playlistSongRepository.deleteAllById(longs);
    }

    public void addSongToPlaylist(Long id_playlist, Long id_song) {
        PlaylistSong entity = new PlaylistSong();
        entity.setPlaylistSongId(new PlaylistSongId(id_playlist, id_song));
        entity.setDayAdded(LocalDateTime.now());
        playlistSongRepository.save(entity);
    }

    public boolean isSongExistsInPlaylist(Long id_playlist, Long idSong) {
        return playlistSongRepository.isSongExistsInPlaylist(id_playlist, idSong);
    }

    public int countSongsByPlaylistId(Long id_playlist) {
        return playlistSongRepository.countSongsByPlaylistId(id_playlist);
    }
}
