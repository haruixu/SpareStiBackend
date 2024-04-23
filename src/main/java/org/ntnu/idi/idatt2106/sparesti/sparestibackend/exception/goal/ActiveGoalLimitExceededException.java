package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal;

public class ActiveGoalLimitExceededException extends RuntimeException {

    public ActiveGoalLimitExceededException() {
        super("A user can only have max 10 active goals");
    }
}
