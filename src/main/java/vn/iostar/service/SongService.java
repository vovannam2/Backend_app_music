package vn.iostar.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.iostar.entity.Album;
import vn.iostar.entity.Song;
import vn.iostar.model.SongModel;
import vn.iostar.repository.SongRepository;
import vn.iostar.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private Cloudinary cloudinary;

    private final SongRepository songRepository;

    private final UserRepository userRepository;

    public SongService(SongRepository songRepository, UserRepository userRepository) {
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    public List<SongModel> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        return convertToSongModelList(songs);
    }
//    public List<Song> getAllSongs() {
//        List<Song> songs = songRepository.findAll();
//        return songs;
//    }

    public void saveSong(Song song) {
        songRepository.save(song);
    }

    public Optional<Song> getSongById(Long id) {
        return songRepository.findById(id);
    }

    public List<Song> getSongViewHigherThanSomeValue(int val) {
        return songRepository.findByViewsGreaterThan(val);
    }

    public List<Song> getSongByAlbum(Album album) {
        return songRepository.findByAlbum(album);
    }

    public List<SongModel> getSongsByKeyWord(String keyword) {
        List<Song> songs = songRepository.findByNameContaining(keyword);
        if (!songs.isEmpty()) {
            List<SongModel> songModels = new ArrayList<>();
            for (Song song : songs) {
                SongModel songModel = new SongModel();
                BeanUtils.copyProperties(song, songModel);
                songModels.add(songModel);
            }
            return songModels;
        } else {
            return new ArrayList<>();
        }
    }

    public void increaseViewOfSong(Long id) {
        songRepository.incrementViewCount(id);
    }

    public Optional<Song> getSongbyId(Long id){
        return  songRepository.findById(id);
    }

    public Page<SongModel> getSongsByMostViews(Pageable pageable) {
        Page<Song> songs = songRepository.findByOrderByViewsDesc(pageable);
        return songs.map(this::convertToSongModel);
    }

    public Page<SongModel> getSongsByMostLikes(Pageable pageable) {
        Page<Song> songs = songRepository.findSongsByMostLikes(pageable);
        return songs.map(this::convertToSongModel);
    }

    public Page<SongModel> getSongsByDayCreated(Pageable pageable) {
        Page<Song> songs = songRepository.findByOrderByDayCreatedDesc(pageable);
        return songs.map(this::convertToSongModel);
    }

    public List<SongModel> convertToSongModelList(List<Song> songs) {
        List<SongModel> songModels = new ArrayList<>();
        for (Song song : songs) {
            SongModel songModel = new SongModel();
            songModel.setIdSong(song.getIdSong());
            System.out.println(song.getIdSong());
            songModel.setName(song.getName());
            songModel.setViews(song.getViews());
            songModel.setDayCreated(song.getDayCreated());
            songModel.setResource(song.getResource());
            songModel.setImage(song.getImage());
            songModel.setArtistId(song.getArtistSongs().get(0).getArtistSongId().getIdArtist());
            songModel.setArtistName(userRepository.getById(song.getArtistSongs().get(0).getArtistSongId().getIdArtist()).getNickname());;
            songModels.add(songModel);
        }
        return songModels;
    }

    public SongModel convertToSongModel(Song song) {
        SongModel songModel = new SongModel();
        songModel.setIdSong(song.getIdSong());
        songModel.setName(song.getName());
        songModel.setViews(song.getViews());
        songModel.setDayCreated(song.getDayCreated());
        songModel.setResource(song.getResource());
        songModel.setImage(song.getImage());
        songModel.setArtistId(song.getArtistSongs().get(0).getArtistSongId().getIdArtist());
        songModel.setArtistName(userRepository.getById(song.getArtistSongs().get(0).getArtistSongId().getIdArtist()).getNickname());
        return songModel;
    }

    public long countSongs() {
        return songRepository.count();
    }

    public String uploadAudio(MultipartFile audioFile) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(audioFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        return (String) result.get("url");
    }

    public void deleteSong(Song song) {
        songRepository.delete(song);
    }

    public List<SongModel> searchSong(String query){
        List<Song> songs = songRepository.searchSong(query);
        if (!songs.isEmpty()) {
            List<SongModel> songModels = new ArrayList<>();
            for (Song song : songs) {
                SongModel songModel = new SongModel();
                BeanUtils.copyProperties(song, songModel);
                songModels.add(songModel);
            }
            return songModels;
        } else {
            return new ArrayList<>();
        }
    }
}
