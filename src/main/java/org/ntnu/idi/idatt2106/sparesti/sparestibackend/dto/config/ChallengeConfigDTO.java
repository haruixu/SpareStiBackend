package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChallengeConfigDTO(
        @NotNull Experience experience,
        @NotNull Motivation motivation,
        @NotNull @Size Set<ChallengeTypeConfigDTO> challengeTypeConfigs)
        implements Serializable {}
