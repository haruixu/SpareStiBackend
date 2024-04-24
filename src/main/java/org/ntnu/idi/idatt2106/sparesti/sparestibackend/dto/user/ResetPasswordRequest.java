package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record ResetPasswordRequest(
        @NotNull(message = "Reset ID cannot be null")
                @NotEmpty(message = "Reset ID cannot be empty")
                @NotBlank(message = "Reset ID cannot be blank")
                String resetID,
        @NotNull(message = "User ID cannot be null")
                @NotEmpty(message = "User ID cannot be empty")
                @NotBlank(message = "User ID cannot be blank")
                Long userID,
        @NotNull(message = "New password cannot be null")
                @NotEmpty(message = "New password cannot be empty")
                @NotBlank(message = "New password cannot be blank")
                String newPassword)
        implements Serializable {}
