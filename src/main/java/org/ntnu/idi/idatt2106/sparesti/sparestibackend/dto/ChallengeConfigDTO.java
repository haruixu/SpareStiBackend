package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import lombok.Value;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChallengeConfigDTO implements Serializable {
    @NotNull Experience experience;
    @NotNull Motivation motivation;
    @NotNull @Size Set<ChallengeTypeConfigDTO> challengeTypeConfigs;
}
