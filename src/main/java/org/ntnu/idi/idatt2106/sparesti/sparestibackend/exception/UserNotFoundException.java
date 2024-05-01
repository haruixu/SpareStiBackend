package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown for finding non-existent user
 *
 * @author Yasin M.
 * @version 1.0
 * @since 19.4.24
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param username Username of non-existent user
     */
    public UserNotFoundException(String username) {
        super("User with username: '" + username + "' not found");
    }
}
