package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown for attempting to find mail that does not exist
 *
 * @author Lars N.
 * @version 1.0
 * @since 20.4.24
 */
public class EmailNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param message Custom message
     */
    public EmailNotFoundException(String message) {
        super(message);
    }
}
