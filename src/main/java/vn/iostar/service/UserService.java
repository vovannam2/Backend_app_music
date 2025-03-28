package vn.iostar.service;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import vn.iostar.auth.registration.RegisterResponse;
import vn.iostar.entity.Album;
import vn.iostar.entity.Role;
import vn.iostar.entity.User;
import vn.iostar.model.AlbumModel;
import vn.iostar.model.ArtistModel;
import vn.iostar.repository.FollowArtistRepository;
import vn.iostar.repository.UserRepository;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    private FollowArtistRepository followArtistRepository;

    private final PasswordEncoder passwordEncoder;

    private final static String EMAIL_NOT_FOUND_MSG = "Email %s not found!";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FollowArtistRepository followArtistRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followArtistRepository = followArtistRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(EMAIL_NOT_FOUND_MSG)));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUserByFields(Long id_user, Map<String, Object> fields) {
        Optional<User> userEntityOptional = userRepository.findByIdUser(id_user);

        if (userEntityOptional.isPresent()) {
            User userEntity = userEntityOptional.get();
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    if (field.getType().equals(Role.class)) {
                        // Handle setting the role field
                        Role roleValue = Role.valueOf((String) value);
                        ReflectionUtils.setField(field, userEntity, roleValue);
                    } else {
                        // For other fields, directly set the value
                        ReflectionUtils.setField(field, userEntity, value);
                    }
                }
            });

            userRepository.save(userEntity);
            return userEntity;
        }

        return null;
    }
    public void updateUserInformation(User user) {
            userRepository.save(user);
    }

    public Optional<User> findByIdUser(Long idUser) {
        return userRepository.findByIdUser(idUser);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public <S extends User> S save(S entity) {
        return userRepository.save(entity);
    }

    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public RegisterResponse changePassword(Long idUser, String oldPassword, String newPassword) {
        Optional<User> optUser = userRepository.findByIdUser(idUser);
        if(optUser.isPresent()) {
            User user = optUser.get();
            if (Objects.equals(passwordEncoder.encode(oldPassword), user.getPassword())){
                return RegisterResponse.builder().message("Password is wrong").error(true).success(false).build();
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return RegisterResponse.builder().message("Change Password Successfully!").error(false).success(true).build();
        }
        return RegisterResponse.builder().message("User Not Valid!").error(true).success(false).build();
    }

    public RegisterResponse changePasswordForgot(String email, String password) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return RegisterResponse.builder().message("Change Password Successfully!").error(false).success(true).build();
        }
        return RegisterResponse.builder().message("User Not Valid!").error(true).success(false).build();
    }

    public List<User> getArtistByKeyword(String keyword) {
        return userRepository.findByNicknameContaining(keyword);
    }

    public void deleteArtist(User artist) {
        userRepository.delete(artist);
    }

    public List<User> findByRoles(List<Role> roles) {
        return userRepository.findByRoles(roles);
    }
    public Page<User> findByRoles(List<Role> roles, Pageable pageable) {
        return userRepository.findByRoles(roles, pageable);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public long countArtists() {
        return userRepository.countArtists();
    }

    public List<Long> getAllFollowers(Long id){
        return followArtistRepository.findUserIdsByArtistId(id);
    }

    public List<ArtistModel> searchArtist(String query) {
        List<User> artists = userRepository.searchArtist(query);
        if (!artists.isEmpty()) {
            List<ArtistModel> artistModels = new ArrayList<>();
            for (User artist : artists) {
                ArtistModel artistModel = new ArtistModel();
                BeanUtils.copyProperties(artist, artistModel);
                artistModels.add(artistModel);
            }
            return artistModels;
        } else {
            return new ArrayList<>();
        }
    }
}
