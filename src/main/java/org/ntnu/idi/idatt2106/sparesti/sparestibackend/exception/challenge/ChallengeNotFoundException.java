package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.challenge;

public class ChallengeNotFoundException extends RuntimeException {
    public ChallengeNotFoundException(Long id) {
        super("Challenge with id: " + id + " was not found");
    }
}
