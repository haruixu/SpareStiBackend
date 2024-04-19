package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Value;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserConfigResponse implements Serializable {
    @NotNull Role role;
    @NotNull ChallengeConfigDTO challengeConfig;
}
