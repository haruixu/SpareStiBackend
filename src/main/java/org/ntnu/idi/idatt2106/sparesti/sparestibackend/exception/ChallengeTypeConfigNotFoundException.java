package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown for attempting to find challenge type config that does not exist
 *
 * @author Yasin M.
 * @version 1.0
 * @since 23.4.24
 */
public class ChallengeTypeConfigNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param type Non-existent type
     */
    public ChallengeTypeConfigNotFoundException(String type) {
        super("No configuration found for challenge type: " + type);
    }
}
