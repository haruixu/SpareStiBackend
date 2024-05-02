package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig}
 * @param role Role of user
 * @param challengeConfig Challenge configuration for user
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserConfigDTO(
        @NotNull(message = "Role cannot be null") Role role,
        @NotNull(message = "Challenge config cannot be null") ChallengeConfigDTO challengeConfig)
        implements Serializable {}
