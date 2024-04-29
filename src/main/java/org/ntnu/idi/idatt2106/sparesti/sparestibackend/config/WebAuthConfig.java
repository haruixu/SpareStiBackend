package org.ntnu.idi.idatt2106.sparesti.sparestibackend.config;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "authn")
@Getter
@Setter
@Configuration
public class WebAuthConfig {
    private String hostName;
    private String display;
    Set<String> origin;
}
