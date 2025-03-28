package vn.iostar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.iostar.entity.Role;
import vn.iostar.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    @Query("SELECT u FROM User u WHERE u.idUser = ?1")
    Optional<User> findByIdUser(Long id_user);
    public Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isActive = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    List<User> findByNicknameContaining(String name);

    @Query("SELECT u FROM User u WHERE u.role IN :roles")
    List<User> findByRoles(List<Role> roles);

    @Query("SELECT u FROM User u WHERE u.role IN :roles")
    Page<User> findByRoles(List<Role> roles, Pageable pageable);

    @Override
    long count();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'ARTIST'")
    long countArtists();
    @Query("SELECT u FROM User u WHERE u.nickname LIKE %:query% AND u.role = 'ARTIST'")
    List<User> searchArtist(String query);

}
