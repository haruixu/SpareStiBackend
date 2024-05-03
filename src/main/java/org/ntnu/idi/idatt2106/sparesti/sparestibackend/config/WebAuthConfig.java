package org.ntnu.idi.idatt2106.sparesti.sparestibackend.config;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configures web auth
 *
 * @author Yasin M.
 * @version 1.0
 * @since 28.4.24
 */
@ConfigurationProperties(prefix = "authn")
@Getter
@Setter
@Configuration
public class WebAuthConfig {

    /**
     * Default constructor
     */
    public WebAuthConfig() {}

    private String hostName;
    private String display;
    Set<String> origin;
}
