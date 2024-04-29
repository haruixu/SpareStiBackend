package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

public class UserHandleNotFoundException extends RuntimeException {
    public UserHandleNotFoundException(String username) {
        super("' " + username + " has not registered any passkey");
    }
}
