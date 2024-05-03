package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config;

/**
 * Exception thrown for when attempting to create Challenge config if user already has one
 *
 * @author Yasin M.
 * @version 1.0
 * @since 23.4.24
 */
public class ChallengeConfigAlreadyExistsException extends RuntimeException {

    /**
     * Constructor for exception
     * @param id Id of user
     */
    public ChallengeConfigAlreadyExistsException(Long id) {
        super("Challenge config for user with id " + id + " already exists");
    }
}
