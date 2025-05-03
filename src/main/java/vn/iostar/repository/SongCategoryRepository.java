package vn.iostar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.iostar.entity.SongCategory;

public interface SongCategoryRepository extends JpaRepository<SongCategory, Long> {

}
