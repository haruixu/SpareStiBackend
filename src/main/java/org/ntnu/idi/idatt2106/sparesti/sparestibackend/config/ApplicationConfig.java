package org.ntnu.idi.idatt2106.sparesti.sparestibackend.config;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Class for instantiating beans used during server runtime
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    /**
     * Creates a bean representing user info
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username ->
                repository
                        .findByUsername(username)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "User with username '" + username + "' not found"));
    }

    /**
     * Bean for authentication
     * @return Authentication object
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Returns the manager for authentication context
     * @param config Config for auth
     * @return Manager for auth context
     * @throws Exception For invalid config of auth context
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Returns bean for BCryptPasswordEncoder that hashes and salts password
     * @return PasswordEncoder object
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
