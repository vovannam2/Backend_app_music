package vn.iostar.service;

import org.springframework.stereotype.Service;
import vn.iostar.entity.Album;
import vn.iostar.repository.AlbumRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }
    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }

    public List<Album> getAlbumByIdArtist(Long idArtist) {
        return albumRepository.getAlbumByIdArtist(idArtist);
    }

    public List<Album> getAlbumByKeyword(String keyword) {
        return albumRepository.findByNameContaining(keyword);
    }

    public Album saveAlbum(Album album) {
        return albumRepository.save(album);
    }

    public int countAlbumsByArtistId(Long idArtist) {
        return albumRepository.countAlbumsByArtistId(idArtist);
    }

    public void deleteAlbum(Album album) {
        albumRepository.delete(album);
    }
}
