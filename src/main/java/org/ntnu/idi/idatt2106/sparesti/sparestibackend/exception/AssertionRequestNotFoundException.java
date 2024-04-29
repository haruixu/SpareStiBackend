package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

public class AssertionRequestNotFoundException extends RuntimeException {
    public AssertionRequestNotFoundException(String username) {
        super("Assertion request not found for: " + username + " Try to login again.");
    }
}
