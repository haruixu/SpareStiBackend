package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation;

/**
 * Exception that is thrown for bad user input
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
public class BadInputException extends RuntimeException {

    /**
     * Constructs an instance of the exception class
     * @param message Custom message that the exception is thrown with
     */
    public BadInputException(String message) {
        super(message);
    }
}
