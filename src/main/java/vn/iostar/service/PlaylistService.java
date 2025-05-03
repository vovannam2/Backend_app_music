package vn.iostar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.iostar.embededId.PlaylistSongId;
import vn.iostar.entity.Playlist;
import vn.iostar.entity.PlaylistSong;
import vn.iostar.entity.Song;
import vn.iostar.entity.User;
import vn.iostar.model.PlaylistModel;
import vn.iostar.model.PlaylistRequest;
import vn.iostar.repository.PlaylistRepository;
import vn.iostar.repository.PlaylistSongRepository;
import vn.iostar.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final String DEFAULT_IMAGE = "https://res.cloudinary.com/dxaobwm8l/image/upload/v1710831081/images/placeholder.jpg";

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongService songService;

    public List<Playlist> getPlaylistByUser(User user) {
        return playlistRepository.findByUser(user);
    }

    public List<PlaylistModel> getPlaylistsByIdUser(Long idUser) {
        List<PlaylistModel> models = new ArrayList<>();
        for (Playlist playlist: playlistRepository.getPlaylistsByIdUser(idUser)) {
            models.add(convertToPlaylistModel(playlist));
        }
        return models;
    }

    public PlaylistModel createPlaylist(PlaylistRequest requestBody) {
        // Check user exists
        User user = userRepository.findById(requestBody.getIdUser()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        //Create playlist first
        Playlist savedPlaylist = new Playlist();
        savedPlaylist.setUser(user);
        savedPlaylist.setName(requestBody.getName());
        savedPlaylist.setImage(DEFAULT_IMAGE);
        savedPlaylist.setDayCreated(LocalDateTime.now());
        savedPlaylist.setPlaylistSongs(null);
        playlistRepository.save(savedPlaylist);

        //Add songs to playlist if songIds is not null
        List<Long> songIds = requestBody.getSongIds();
        if (songIds != null) {
            List<PlaylistSong> playlistSongs = new ArrayList<>();
            for (Long idSong: songIds) {
                PlaylistSong playlistSong = new PlaylistSong();
                playlistSong.setPlaylistSongId(new PlaylistSongId(savedPlaylist.getIdPlaylist(), idSong));
                playlistSong.setDayAdded(savedPlaylist.getDayCreated());
                playlistSongs.add(playlistSongRepository.save(playlistSong));
            }
            savedPlaylist.setPlaylistSongs(playlistSongs);
        }
        setPlaylistImageByFirstSongImage(savedPlaylist.getIdPlaylist());
        return convertToPlaylistModel(savedPlaylist);

    }

    public Optional<Playlist> getPlaylistById(Long idPlaylist) {
        return playlistRepository.findById(idPlaylist);
    }

    public void deletePlaylist(Playlist playlist) {
        playlistRepository.delete(playlist);
    }

    public PlaylistModel convertToPlaylistModel(Playlist playlist) {
        PlaylistModel playlistModel = new PlaylistModel();
        playlistModel.setIdPlaylist(playlist.getIdPlaylist());
        playlistModel.setIdUser(playlist.getUser().getIdUser());
        playlistModel.setName(playlist.getName());
        playlistModel.setDayCreated(playlist.getDayCreated());
        playlistModel.setImage(playlist.getImage());
        playlistModel.setSongs(songService.convertToSongModelList(
                playlistSongRepository.findAllByPlaylistSongId(playlist.getIdPlaylist()))
        );
        return playlistModel;
    }

    public void setPlaylistImageByFirstSongImage(Long idPlaylist) {
        Playlist playlist = playlistRepository.findById(idPlaylist).orElseThrow(
                () -> new RuntimeException("Playlist not found")
        );
        if (playlist.getPlaylistSongs().isEmpty()) {
            playlist.setImage(DEFAULT_IMAGE);
        } else {
            Long idSong = playlist.getPlaylistSongs().get(0).getPlaylistSongId().getIdSong();
            Song song = songService.getSongById(idSong).get();
            playlist.setImage(song.getImage());
        }
        playlistRepository.save(playlist);
    }

    public void sortPlaylistSongsByDayAdded(Playlist playlist) {
        playlist.getPlaylistSongs().sort((o1, o2) -> o2.getDayAdded().compareTo(o1.getDayAdded()));
    }

    public boolean isPlaylistNameExists(String name) {
        Optional<Playlist> playlist = playlistRepository.findPlaylistByName(name);
        return playlist.isPresent();
    }

    public void savePlaylist(Playlist playlist) {
        playlistRepository.save(playlist);
    }
}
