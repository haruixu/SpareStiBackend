package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal;

/**
 * Exception for goal that is not active
 *
 * @author Harry X.
 * @version 1.0
 * @since 23.4.24
 */
public class NotActiveGoalException extends RuntimeException {

    /**
     * Constructor for exception
     * @param id Id of non-active goal
     */
    public NotActiveGoalException(Long id) {
        super("Goal with id '" + id + "' is not an active goal");
    }
}
