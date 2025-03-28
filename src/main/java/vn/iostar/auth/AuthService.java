package vn.iostar.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.iostar.auth.authentication.AuthenticationRequest;
import vn.iostar.auth.authentication.AuthenticationResponse;
import vn.iostar.auth.email.EmailService;
import vn.iostar.auth.otp.OtpResponse;
import vn.iostar.auth.refreshToken.RefreshTokenRequest;
import vn.iostar.auth.refreshToken.RefreshTokenResponse;
import vn.iostar.auth.email.EmailValidator;
import vn.iostar.auth.registration.RegisterRequest;
import vn.iostar.auth.registration.RegisterResponse;
import vn.iostar.entity.*;
import vn.iostar.model.ResponseMessage;
import vn.iostar.service.ConfirmationTokenService;
import vn.iostar.repository.UserRepository;
import vn.iostar.service.RefreshTokenService;
import vn.iostar.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
 

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private final EmailValidator emailValidator;

    private final AuthenticationManager authenticationManager;
    public RegisterResponse register(RegisterRequest request) {
        Boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail) {
            throw new IllegalStateException("Email Not Valid!");
        }
        Optional<User> optUser = repository.findByEmail(request.getEmail());
        if(optUser.isEmpty()) {
            var user = User.builder()
                    .nickname(request.getNickName())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phoneNumber(request.getPhoneNumber())
                    .avatar("https://th.bing.com/th/id/OIP.bWllJuCbia6Vbt18CzJWQQHaHY?w=1005&h=1002&rs=1&pid=ImgDetMain")
                    .role(Role.USER)
                    .provider(Provider.DATABASE)
                    .isActive(false)
                    .build();
            repository.save(user);

            String token = generateOTP();
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user

            );
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            emailService.send(request.getEmail(), buildEmailOTP(request.getFirstName() + " " + request.getLastName(), token));
            String message = "Register Successfully! Please Check Email To See OTP!";
//            if(request.getRole() == null) {
//                message = "Register Successfully! Please Wait Admin Confirm. After Confirmed, Notification Will Be Sended To Your Email";
//            }
            return RegisterResponse.builder()
                    .message(message)
                    .error(false)
                    .success(true)
                    .build();
        }
        else {
            return RegisterResponse.builder()
                    .message("Email Already Taken!")
                    .error(true)
                    .success(false)
                    .build();
        }
    }

    
    public OtpResponse confirmToken(String token, String type) {
        Optional<ConfirmationToken> otpConfirmationToken = confirmationTokenService.getToken(token);
        if(otpConfirmationToken.isPresent()) {
            ConfirmationToken confirmationToken = otpConfirmationToken.get();
            if(confirmationToken.getConfirmedAt() != null) {
                return OtpResponse.builder()
                        .message("Email Already Confirmed!")
                        .error(true)
                        .success(false)
                        .build();
            }
            LocalDateTime expiredAt = confirmationToken.getExpiredAt();
            if(expiredAt.isBefore(LocalDateTime.now())) {
                return OtpResponse.builder()
                        .message("Token Expired! Please User New Token!")
                        .error(true)
                        .success(false)
                        .build();
            }
            confirmationTokenService.setConfirmedAt(token);
            if(type.equals("confirm")) {
                userService.enableUser(confirmationToken.getUser().getEmail());
            }
            return OtpResponse.builder().message("Successfully! Confirmed!").id(confirmationToken.getUser().getIdUser()).type(type).error(false).success(true).build();
        }
        return OtpResponse.builder().message("Token Not Valid!").error(true).success(false).build();
    }

    
    private String buildEmailOTP(String name, String otp) {
        return "<html>\n" +
                "  <head>\n" +
                "    <style>\n" +
                "      body {\n" +
                "        font-family: Arial, sans-serif;\n" +
                "        background-color: #f4f4f4;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "      .container {\n" +
                "        max-width: 600px;\n" +
                "        margin: 50px auto;\n" +
                "        padding: 20px;\n" +
                "        background-color: #fff;\n" +
                "        border-radius: 10px;\n" +
                "        box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                "      }\n" +
                "      h1 {\n" +
                "        color: #333;\n" +
                "      }\n" +
                "      h2 {\n" +
                "        color: #555;\n" +
                "      }\n" +
                "      span {\n" +
                "        color: #ff0000;\n" +
                "        font-weight: bold;\n" +
                "        letter-spacing: 2px;\n" +
                "        font-size: 24px;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"container\">\n" +
                "      <h1>Xin Chào, " + name + "!</h1>\n" +
                "      <h3>Cảm ơn bạn đã đăng ký tài khoản cho ứng dụng của chúng tôi!</h3>\n" +
                "      <h2>Mã OTP để xác nhận tài khoản của bạn là: <span>" + otp + "</span></h2>\n" +
                "      <h3>Trân trọng!</h3>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
    }


   

    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }

    public ResponseMessage sendEmail(String email) {
        Optional<User> optUser = userService.getUserByEmail(email);
        if(optUser.isPresent()) {
            User user = optUser.get();
            String token = generateOTP();
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );
            Optional<ConfirmationToken> optConfirmationToken = confirmationTokenService.getTokenByUser(user);
            optConfirmationToken.ifPresent(value -> confirmationTokenService.delete(value));
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            emailService.send(email, buildEmailOTP(user.getFirstName() + " " + user.getLastName(), token));
            return ResponseMessage.builder()
                    .message("Successfully! Please Check Email To See OTP!")
                    .error(false)
                    .success(true)
                    .build();
        }
        return ResponseMessage.builder()
                .message("Email Not Register! Please Enter Another Email!")
                .error(true)
                .success(false)
                .build();
    }

   
}
