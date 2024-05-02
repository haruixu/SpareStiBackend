package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception;

import jakarta.validation.constraints.NotNull;

/**
 * Exception thrown when attempting to create type config that already exists
 *
 * @author Yasin M.
 * @since 23.4.24
 * @version 1.0
 */
public class ChallengeTypeConfigAlreadyExistsException extends RuntimeException {

    /**
     * Constructor for exception
     * @param id User id
     * @param type Already existing type
     */
    public ChallengeTypeConfigAlreadyExistsException(Long id, @NotNull String type) {
        super(
                "Challenge type config already exists for user with "
                        + id
                        + " and config type "
                        + type);
    }
}
