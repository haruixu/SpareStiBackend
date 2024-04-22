package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Handles business logic related to the user entity. Adds an extra layer abstraction between
 * controller and the data layer.
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Persists a user entity
     * @param user User entity
     * @return The saved user entity
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user from a given username.
     * @param username Username used for finding a user
     * @return User entity with matching username
     * @throws UserNotFoundException If no user has the given username
     */
    public User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * Determines whether a user with the given username exists
     * @param username The username
     * @return True, if the user exists.
     */
    public boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Determines whether a user with the given email exists
     * @param email Email
     * @return True, if a user with the given email exists
     */
    public boolean userExistByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
