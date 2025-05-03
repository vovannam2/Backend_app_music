package vn.iostar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.iostar.response.Response;
import vn.iostar.entity.Role;
import vn.iostar.entity.User;
import vn.iostar.model.ArtistModel;
import vn.iostar.model.PlaylistModel;
import vn.iostar.model.SongModel;
import vn.iostar.service.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PlaylistService playlistService;


    @Autowired
    private SongLikedService songLikedService;

    @Autowired
    private FollowArtistService followArtistService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        Response res = new Response();
        res.setError(false);
        res.setSuccess(true);
        res.setMessage("Get All Users Successfully!");
        res.setData(userService.findAll());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/users/count")
    public ResponseEntity<?> getUserCount() {
        Response res = new Response();
        res.setError(false);
        res.setSuccess(true);
        res.setMessage("Get User Count Successfully!");
        res.setData(userService.countUsers());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/users/artists/count")
    public ResponseEntity<?> getArtistsCount() {
        Response res = new Response();
        res.setError(false);
        res.setSuccess(true);
        res.setMessage("Get User Count Successfully!");
        res.setData(userService.countArtists());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<?> getAllUsersNotAdmin() {
        Response res = new Response();
        res.setError(false);
        res.setSuccess(true);
        res.setMessage("Get All Users Successfully!");
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ARTIST);
        roles.add(Role.USER);
        res.setData(userService.findByRoles(roles));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<?> getUserById(@PathVariable("idUser") Long idUser) {
        Response res = new Response();
        res.setError(false);
        res.setSuccess(true);
        res.setMessage("Get All Users Successfully!");
        res.setData(userService.findByIdUser(idUser));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) {
        Response res = new Response();
        res.setError(false);
        res.setSuccess(true);
        res.setMessage("Get User Successfully!");
        res.setData(userService.getUserByEmail(email));
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestPart("idUser") Long idUser, @RequestPart("imageFile") @Nullable MultipartFile imageFile, @RequestPart("firstName") String firstName,@RequestPart("lastName") String lastName, @RequestPart("gender") int gender) throws IOException {
        Optional<User> optUser = userService.findByIdUser(idUser);
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.setGender(gender);
            user.setFirstName(firstName.replace("\"", ""));
            user.setLastName(lastName.replace("\"", ""));
            if(imageFile != null) {
                String imageUrl = imageService.uploadImage(imageFile);
                user.setAvatar(imageUrl);
            }
            userService.save(user);
            Response res = new Response();
            res.setSuccess(true);
            res.setMessage("Update Success!");
            res.setError(false);
            res.setData(user);
            return ResponseEntity.ok(res);
        }
        Response response = new Response(false, true, "Update Fail!", null);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/artist/update")
    public ResponseEntity<?> updateArtist(@RequestPart("idArtist") Long idArtist, @RequestPart("imageFile") @Nullable MultipartFile imageFile, @RequestPart("nickname") String nickname, @RequestPart("gender") int gender) throws IOException {
        Optional<User> optUser = userService.findByIdUser(idArtist);
        nickname = nickname.replace("\"", "");
        if(optUser.isPresent()) {
            User user = optUser.get();
            user.setNickname(nickname);
            user.setGender(gender);
            if(imageFile != null) {
                String imageUrl = imageService.uploadImage(imageFile);
                user.setAvatar(imageUrl);
            }
            userService.save(user);
            Response res = new Response();
            res.setSuccess(true);
            res.setMessage("Update Success!");
            res.setError(false);
            res.setData(user);
            return ResponseEntity.ok(res);
        }
        Response response = new Response(false, true, "Update Fail!", null);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/{idUser}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable("idUser") Long idUser, @RequestBody Map<String, String> reqBody) {
        String oldPassword = reqBody.get("oldPassword");
        String newPassword = reqBody.get("newPassword");
        return ResponseEntity.ok(userService.changePassword(idUser, oldPassword, newPassword));
    }

    @PatchMapping("/user/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> reqBody) {
        String password = reqBody.get("password");
        String email = reqBody.get("email");
        return ResponseEntity.ok(userService.changePasswordForgot(email, password));
    }
  
    @GetMapping("/user/{id_user}/playlists")
    public ResponseEntity<?> getPlaylistsByIdUser(@PathVariable("id_user") Long idUser) {
        List<PlaylistModel> playlists = playlistService.getPlaylistsByIdUser(idUser);
        Response response = new Response(true, false, "Get Playlists Success!", playlists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id_user}/liked-songs")
    public ResponseEntity<?> getLikedSongsByIdUser(@PathVariable("id_user") Long idUser) {
        List<SongModel> songs = songLikedService.getLikedSongsByIdUser(idUser);
        Response response = new Response(true, false, "Get Liked Songs Success!", songs);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/{id_user}/not-liked-songs")
    public ResponseEntity<?> getNotLikedSongsByIdUser(@PathVariable("id_user") Long idUser) {
        List<SongModel> songs = songLikedService.getNotLikedSongsByIdUser(idUser);
        Response response = new Response(true, false, "Get Not Liked Songs Success!", songs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/searchArtist")
    public ResponseEntity<?> searchArtist(@RequestParam("query") String query) {
        List<ArtistModel> artists = userService.searchArtist(query);
        Response response = new Response(true, false, "Find artists Success", artists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id_user}/is-followed-artist")
    public ResponseEntity<?> isUserFollowedArtist(@PathVariable("id_user") Long idUser, @RequestParam("id_artist") Long idArtist) {
        boolean isFollowed = followArtistService.existsByArtistIdAndUserId(idArtist, idUser);
        Response response = new Response(true, false, "Check Successfully!", isFollowed);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{id_user}/follow-artist")
    public ResponseEntity<?> followArtist(@PathVariable("id_user") Long idUser, @RequestParam("id_artist") Long idArtist) {
        if (followArtistService.existsByArtistIdAndUserId(idArtist, idUser)){
            followArtistService.deleteFollowArtist(idArtist, idUser);
            Response response = new Response(true, false, "Unfollow Artist Successfully!", false);
            return ResponseEntity.ok(response);
        }

        Response response = new Response(false, true, "Follow Artist Failed!", false);
        if (followArtistService.saveFollowArtist(idArtist, idUser) != null) {
            response = new Response(true, false, "Follow Artist Successfully!", true);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/{idUser}")
    public ResponseEntity<?> updateUserByFields(@PathVariable("idUser") Long idUser, @RequestBody Map<String, Object> fields) {
        User user = userService.updateUserByFields(idUser, fields);
        Response response = new Response(true, false, "Update Success!", user);
        return ResponseEntity.ok(response);
    }
}
