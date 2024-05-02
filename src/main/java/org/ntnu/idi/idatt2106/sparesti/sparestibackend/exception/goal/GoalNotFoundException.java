package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal;

/**
 * Exception for goal not found
 *
 * @author Harry X.
 * @version 1.0
 * @since 23.4.24
 */
public class GoalNotFoundException extends RuntimeException {

    /**
     * Constructor for exception
     * @param id Id of non-existent goal
     */
    public GoalNotFoundException(Long id) {
        super("Goal with id '" + id + "' not found for current user");
    }
}
