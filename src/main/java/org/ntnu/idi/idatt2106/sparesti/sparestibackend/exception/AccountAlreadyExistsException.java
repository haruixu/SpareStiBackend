package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception that is thrown when creating an account that already exists
 *
 * @author Lars M.L.N
 * @version 1.0
 * @since 24.4.24
 */
public class AccountAlreadyExistsException extends RuntimeException {

    /**
     * Constructs an instance of the exception class
     * @param message Custom message that the exception is thrown with
     */
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
