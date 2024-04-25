package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config;

import jakarta.validation.constraints.NotNull;

public class ChallengeTypeConfigAlreadyExistsException extends RuntimeException {
    public ChallengeTypeConfigAlreadyExistsException(Long id, @NotNull String type) {
        super(
                "Challenge type config already exists for user with "
                        + id
                        + " and config type "
                        + type);
    }
}
