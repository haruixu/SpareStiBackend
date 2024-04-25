package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.ChallengeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.Motivation;

/**
 * DTO for {@link ChallengeConfig}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeConfigDTO(
        @NotNull(message = "Experience cannot be null") Experience experience,
        @NotNull(message = "Motivation cannot be null") Motivation motivation,
        @NotNull(message = "Challenge type configurations cannot be null")
                @Size(min = 1, message = "At least one challenge type configuration is required")
                Set<ChallengeTypeConfigDTO> challengeTypeConfigs)
        implements Serializable {}
