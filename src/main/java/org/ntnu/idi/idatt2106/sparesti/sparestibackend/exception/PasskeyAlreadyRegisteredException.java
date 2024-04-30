package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

public class PasskeyAlreadyRegisteredException extends RuntimeException {
    public PasskeyAlreadyRegisteredException(String username) {
        super("Passkey already registered: " + username);
    }
}
