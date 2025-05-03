package vn.iostar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.iostar.entity.SongCategory;
import vn.iostar.repository.SongCategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SongCategoryService {
    @Autowired
    private SongCategoryRepository songCategoryRepository;

    public List<SongCategory> findAll() {
        return songCategoryRepository.findAll();
    }

    public Optional<SongCategory> findById(Long id) {
        return songCategoryRepository.findById(id);
    }
}
