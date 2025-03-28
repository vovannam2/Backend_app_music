package vn.iostar.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "nickname", nullable = true, columnDefinition = "nvarchar(1000)")
    private String nickname;

    @Column(name = "first_name", nullable = false, columnDefinition = "nvarchar(1000)")
    private String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "nvarchar(1000)")
    private String lastName;

    @Column(name = "phone_number", nullable = false, columnDefinition = "varchar(20)")
    private String phoneNumber;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(1000)")
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(1000)")
    private String password;

    @Column(name = "gender", nullable = false)
    private int gender;

    @Column(name = "avatar", columnDefinition = "varchar(1000)")
    private String avatar;

    @Column(name = "introduction", nullable = true, columnDefinition = "nvarchar(10000)")
    private String introduction;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentLiked> commentLikeds;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Playlist> playlists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SongComment> songComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SongLiked> songLikeds;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FollowArtist> followedArtists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ConfirmationToken> confirmationTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Album> albums;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<ArtistSong> artistSongs;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<FollowArtist> followArtists;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Getter
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return Collections.singleton(authority);
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
