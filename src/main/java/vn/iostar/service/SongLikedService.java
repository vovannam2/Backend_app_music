package vn.iostar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import vn.iostar.embededId.SongLikedId;
import vn.iostar.entity.Song;
import vn.iostar.entity.SongLiked;
import vn.iostar.entity.User;
import vn.iostar.model.SongLikedRequest;
import vn.iostar.model.SongModel;
import vn.iostar.repository.SongLikedRepository;
import vn.iostar.repository.UserRepository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SongLikedService {

    @Autowired
    private SongLikedRepository songLikedRepository;

    @Autowired
    private SongService songService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public SongLikedService(SongLikedRepository songLikedRepository) {
        this.songLikedRepository = songLikedRepository;
    }

    @Query("SELECT COUNT(s) FROM SongLiked s WHERE s.songLikedId.idSong = ?1")
    public Long countLikesBySongId(Long songId) {
        return songLikedRepository.countLikesBySongId(songId);
    }

    @Query("SELECT COUNT(s) FROM SongLiked s WHERE s.songLikedId.idSong = ?1 AND s.songLikedId.idUser = ?2")
    public Long countLikesBySongIdAndUserId(Long songId, Long userId) {
        return songLikedRepository.countLikesBySongIdAndUserId(songId, userId);
    }

    public boolean isUserLikedSong(Long songId, Long userId) {
        return songLikedRepository.isUserLikedSong(songId, userId);
    }

    public <S extends SongLiked> S save(S entity) {
        return songLikedRepository.save(entity);
    }

    public void deleteById(Long aLong) {
        songLikedRepository.deleteById(aLong);
    }

    public void delete(SongLiked entity) {
        songLikedRepository.delete(entity);
    }

    public void deleteBySongLikedId(SongLikedId songLikedId) {
        songLikedRepository.deleteBySongLikedId(songLikedId);
    }

    @Transactional
    public boolean toggleLike(Long songId, Long userId) {
        if (isUserLikedSong(songId, userId)) {
            songLikedRepository.deleteBySongLikedId(new SongLikedId(userId, songId));
            return false;
        } else {
            SongLiked songLiked = new SongLiked();
            songLiked.setSongLikedId(new SongLikedId(userId, songId));
            songLiked.setDayLiked(LocalDateTime.now());
            songLikedRepository.save(songLiked);
            return true;
        }
    }

    public List<SongModel> getLikedSongsByIdUser(Long idUser) {
        List<Song> songs = songLikedRepository.getLikedSongsByIdUser(idUser);
        return songService.convertToSongModelList(songs);
    }

    public List<SongModel> getNotLikedSongsByIdUser(Long idUser) {
        List<Song> songs = songLikedRepository.getNotLikedSongsByIdUser(idUser);
        return songService.convertToSongModelList(songs);
    }

    public void addSongToFavourite(SongLikedRequest requestBody) {
        // Check user exists
        User user = userRepository.findById(requestBody.getIdUser()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        for (Long idSong: requestBody.getSongIds()) {
                SongLiked songLiked = new SongLiked();
                songLiked.setSongLikedId(new SongLikedId(user.getIdUser(), idSong));
                songLiked.setDayLiked(LocalDateTime.now());
                songLikedRepository.save(songLiked);
        }
    }
}
