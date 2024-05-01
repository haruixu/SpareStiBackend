package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown for when challenge could not be found
 *
 * @author Yasin M.
 * @version 1.0
 * @since 22.4.2024
 */
public class ChallengeNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param id Id of non-existent challenge
     */
    public ChallengeNotFoundException(Long id) {
        super("Challenge with id: " + id + " was not found");
    }
}
