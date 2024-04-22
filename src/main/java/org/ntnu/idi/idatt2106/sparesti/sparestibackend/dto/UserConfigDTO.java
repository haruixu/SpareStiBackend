package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserConfigDTO(@NotNull Role role, @NotNull ChallengeConfigDTO challengeConfig)
        implements Serializable {}
