package org.ntnu.idi.idatt2106.sparesti.sparestibackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Config class to enable CORS in requests.
 *
 * @author Harry L.X
 * @version 1.0
 * @since 24.4.24
 */
@Configuration
public class CrossOriginConfig {

    /**
     * Default constructor
     */
    public CrossOriginConfig() {}

    /**
     * Bean that configures CORS policy. Disable CORS-block on all types of requests on all endpoints
     * @return Bean for configuring CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
            }
        };
    }
}
