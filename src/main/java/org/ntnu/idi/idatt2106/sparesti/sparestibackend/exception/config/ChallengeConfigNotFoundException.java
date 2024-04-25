package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config;

public class ChallengeConfigNotFoundException extends RuntimeException {
    public ChallengeConfigNotFoundException(Long id) {
        super("Could not find challenge config for user with id " + id);
    }
}
