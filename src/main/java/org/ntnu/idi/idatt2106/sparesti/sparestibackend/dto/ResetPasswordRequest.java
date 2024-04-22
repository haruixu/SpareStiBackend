package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record ResetPasswordRequest(
        @NotNull @NotEmpty @NotBlank String resetID,
        @NotNull @NotEmpty @NotBlank Long userID,
        @NotNull @NotEmpty @NotBlank String newPassword)
        implements Serializable {}
