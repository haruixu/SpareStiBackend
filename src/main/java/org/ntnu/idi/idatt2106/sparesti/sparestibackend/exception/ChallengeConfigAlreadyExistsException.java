package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

public class ChallengeConfigAlreadyExistsException extends RuntimeException {
    public ChallengeConfigAlreadyExistsException(Long id) {
        super("Challenge config for user with id " + id + " already exists");
    }
}
