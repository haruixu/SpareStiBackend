package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal;

public class NotActiveGoalException extends RuntimeException {
    public NotActiveGoalException(Long id) {
        super("Goal with id '" + id + "' is not an active goal");
    }
}
