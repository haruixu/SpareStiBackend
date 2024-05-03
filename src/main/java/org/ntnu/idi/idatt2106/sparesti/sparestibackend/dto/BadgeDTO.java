package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Badge}
 * @param id Id of badge
 * @param title title
 * @param description description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BadgeDTO(
        @NotNull @Positive Long id,
        @NotNull @NotEmpty @NotBlank String title,
        @NotNull @NotEmpty @NotBlank String description)
        implements Serializable {}
