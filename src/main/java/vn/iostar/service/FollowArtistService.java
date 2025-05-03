package vn.iostar.service;

import org.springframework.stereotype.Service;
import vn.iostar.embededId.FollowArtistId;
import vn.iostar.entity.FollowArtist;
import vn.iostar.repository.FollowArtistRepository;

import java.util.List;

@Service
public class FollowArtistService {
    private final FollowArtistRepository followArtistRepository;

    public FollowArtistService(FollowArtistRepository followArtistRepository) {
        this.followArtistRepository = followArtistRepository;
    }

    public List<Long> findUserIdsByArtistId(Long artistId) {
        return followArtistRepository.findUserIdsByArtistId(artistId);
    }

    public boolean existsByArtistIdAndUserId(long artistId, long userId) {
        return followArtistRepository.existsByArtistIdAndUserId(artistId, userId);
    }

    public FollowArtist saveFollowArtist(Long artistId, Long userId) {
        FollowArtist followArtist = new FollowArtist();
        FollowArtistId followArtistId = new FollowArtistId();
        followArtistId.setIdArtist(artistId);
        followArtistId.setIdUser(userId);
        followArtist.setFollowArtistId(followArtistId);
        return followArtistRepository.save(followArtist);
    }

    public void deleteFollowArtist(Long artistId, Long userId) {
        FollowArtistId followArtistId = new FollowArtistId();
        followArtistId.setIdArtist(artistId);
        followArtistId.setIdUser(userId);
        followArtistRepository.deleteById(followArtistId);
    }
}
