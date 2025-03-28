package vn.iostar.auth.registration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iostar.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String nickName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int gender;
    private String phoneNumber;
    private Role role;
    private String avatar;
}
