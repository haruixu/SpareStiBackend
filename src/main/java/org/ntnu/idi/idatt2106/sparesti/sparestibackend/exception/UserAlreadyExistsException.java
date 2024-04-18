package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
