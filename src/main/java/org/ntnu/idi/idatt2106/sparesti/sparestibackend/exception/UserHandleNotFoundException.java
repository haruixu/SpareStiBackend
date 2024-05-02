package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown for when user handle was not found
 *
 * @author Yasin M.
 * @version 1.0
 * @since 29.4.24
 */
public class UserHandleNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param username username of the non-existent user handle
     */
    public UserHandleNotFoundException(String username) {
        super("' " + username + " has not registered any passkey");
    }
}
