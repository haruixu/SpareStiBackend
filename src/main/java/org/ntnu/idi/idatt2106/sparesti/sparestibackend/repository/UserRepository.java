package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for communicating with the data layer
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user from a given username
     * @param username Username
     * @return Optional user
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user from a given email
     * @param email Email
     * @return Optional email
     */
    Optional<User> findByEmail(String email);
}
