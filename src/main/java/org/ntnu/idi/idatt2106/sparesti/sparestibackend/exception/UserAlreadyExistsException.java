package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception that is thrown when creating a user that already exists
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs an instance of the exception class
     * @param message Custom message that the exception is thrown with
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
