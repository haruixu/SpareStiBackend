package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config;

/**
 * Exception thrown for attempting to find non-existent Challenge config
 *
 * @author Yasin M.
 * @version 1.0
 * @since 23.4.24
 */
public class ChallengeConfigNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param id Id of config that exception was thrown for
     */
    public ChallengeConfigNotFoundException(Long id) {
        super("Could not find challenge config for user with id " + id);
    }
}
