package vn.iostar.config;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary("cloudinary://514747933839148:8Um-dy8v2p4rhuzy2WAsobrHF4g@dv3gj6qre");
    }
}
