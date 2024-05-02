package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

/**
 * Exception thrown for when attempting to complete challenge that was already completed
 *
 * @author Yasin M.
 * @version 1.0
 * @since 24.4.24
 */
public class ChallengeAlreadyCompletedException extends RuntimeException {

    /**
     * Constructor for exception
     * @param challengeId Id of challenge that already was completed
     */
    public ChallengeAlreadyCompletedException(Long challengeId) {
        super("Challenge already completed: " + challengeId);
    }
}
