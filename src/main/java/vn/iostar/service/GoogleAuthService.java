package vn.iostar.service;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Service
public class GoogleAuthService {

    @Value("classpath:service-account.json")
    private Resource serviceAccountResource;

    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(serviceAccountResource.getInputStream())
                .createScoped(Collections.singleton(MESSAGING_SCOPE));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
