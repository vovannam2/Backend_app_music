package vn.iostar.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.iostar.entity.ArtistSong;
import vn.iostar.entity.Song;
import vn.iostar.entity.User;
import vn.iostar.model.SongModel;
import vn.iostar.repository.ArtistSongRepository;

import java.util.List;

@Service
public class ArtistSongService {
    private final ArtistSongRepository artistSongRepository;

    @Autowired
    private SongService songService;
    @Autowired
    public ArtistSongService(ArtistSongRepository artistSongRepository) {
        this.artistSongRepository = artistSongRepository;
    }

    public Page<SongModel> findAllSongsByArtistId(Long id_artist, Pageable pageable) {
        Page<Song> songs = artistSongRepository.findAllSongsByArtistId(id_artist, pageable);
        return songs.map(songService::convertToSongModel);
    }

    public List<User> findArtistBySongId(Long idSong){
        return artistSongRepository.findArtistBySongId(idSong);
    }

    public List<Song> getSongsOfArtistDesc(Long idArtist) {
        return artistSongRepository.findSongsByArtistIdOrderByViewCountDesc(idArtist);
    }

    public int countSongsByArtistId(Long idArtist) {
        return artistSongRepository.countSongsByArtistId(idArtist);
    }

    public void save(ArtistSong artistSong) {
        artistSongRepository.save(artistSong);
    }

    public int countTotalViewsByArtistId(Long idArtist) {
        return artistSongRepository.countTotalViewsByArtistId(idArtist);
    }

    public int countUsersByArtistId(Long idArtist) {
        return artistSongRepository.countUsersByArtistId(idArtist);
    }

    public int countCommentsByArtistId(Long idArtist) {
        return artistSongRepository.countCommentsByArtistId(idArtist);
    }
}
