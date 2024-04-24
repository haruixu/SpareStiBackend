package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception that is thrown when putting to an account that does not exist
 *
 * @author Lars M.L.N
 * @version 1.0
 * @since 24.4.24
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * Constructs an instance of the exception class
     * @param message Custom message that the exception is thrown with
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}
