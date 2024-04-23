package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal;

public class GoalNotFoundException extends RuntimeException {
    public GoalNotFoundException(Long id) {
        super("Goal with id '" + id + "' not found for current user");
    }
}
