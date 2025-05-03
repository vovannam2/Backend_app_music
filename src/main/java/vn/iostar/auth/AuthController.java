package vn.iostar.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.iostar.auth.authentication.AuthenticationRequest;
import vn.iostar.auth.email.EmailService;
import vn.iostar.auth.refreshToken.RefreshTokenRequest;
import vn.iostar.auth.registration.RegisterRequest;
import vn.iostar.model.ResponseMessage;
import vn.iostar.service.RefreshTokenService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String message = payload.get("message");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Send Email Successfully!");
        responseMessage.setSuccess(true);
        responseMessage.setError(false);
        emailService.send(email, message);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token, @RequestParam("type") String type) {
        return ResponseEntity.ok(service.confirmToken(token, type));
    }

    
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/authenticate-oauth")
    public ResponseEntity<?> authenticateOauth(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.OAuthLogin(request));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(service.refreshToken(refreshTokenRequest));
    }
}
    
    
