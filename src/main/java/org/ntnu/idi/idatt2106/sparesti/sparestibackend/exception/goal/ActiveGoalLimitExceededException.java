package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal;

/**
 * Exception for active goal limit (10)
 *
 * @author Harry X.
 * @version 1.0
 * @since 23.4.24
 */
public class ActiveGoalLimitExceededException extends RuntimeException {

    /**
     * Constructor for exception
     */
    public ActiveGoalLimitExceededException() {
        super("A user can only have max 10 active goals");
    }
}
