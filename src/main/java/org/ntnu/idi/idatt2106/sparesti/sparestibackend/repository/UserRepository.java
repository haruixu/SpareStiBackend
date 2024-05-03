package org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository;

import com.yubico.webauthn.data.ByteArray;
import java.util.Optional;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for communicating with the data layer related to the user entity
 *
 * @author Harry L.X and Lars M.L.N
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

    /**
     * Finds a user based on their handle, which acts as a unique identifier for the user
     * @param handle Unique identifier of the user, used for biometric registration/login
     * @return User with the matching handle
     */
    User findByHandle(ByteArray handle);
}
