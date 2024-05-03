package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown for when assertion request was not found
 *
 * @author Yasin M.
 * @version 1.0
 * @since 29.4.24
 */
public class AssertionRequestNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param username Username of user
     */
    public AssertionRequestNotFoundException(String username) {
        super("Assertion request not found for: " + username + " Try to login again.");
    }
}
