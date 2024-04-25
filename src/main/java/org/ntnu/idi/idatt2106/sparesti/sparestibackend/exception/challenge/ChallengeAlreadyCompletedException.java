package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.challenge;

public class ChallengeAlreadyCompletedException extends RuntimeException {
    public ChallengeAlreadyCompletedException(Long challengeId) {
        super("Challenge already completed: " + challengeId);
    }
}
