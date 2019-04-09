package ru.sua.security.resource.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties("security")
@Data
public class SecurityPropertiesFromFile {

    private JwtProperties jwt;

    @Data
    static class JwtProperties {

        private Resource publicKey;

    }

}
