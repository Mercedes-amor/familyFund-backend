package com.familyfund.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dz2owkkwa",
                "api_key", "623948982551927",
                "api_secret", "LwJ6AdpGL2LAxyVLKqcA-HgO548",
                "secure", true
        ));
    }
}
