package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown for attempting to register already registered passkey
 *
 * @author Yasin M.
 * @version 1.0
 * @since 29.4.24
 */
public class PasskeyAlreadyRegisteredException extends RuntimeException {

    /**
     * Constructor for exception
     * @param username Username of already registered passkey
     */
    public PasskeyAlreadyRegisteredException(String username) {
        super("Passkey already registered: " + username);
    }
}
