package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for resetting password
 * @param resetID ID of reset password entry
 * @param userID User ID
 * @param newPassword new password
 */
public record ResetPasswordRequest(
        @NotNull(message = "Reset ID cannot be null")
                @NotBlank(message = "Reset ID cannot be blank")
                String resetID,
        @NotNull(message = "User ID cannot be null") Long userID,
        @NotNull(message = "New password cannot be null")
                @NotBlank(message = "New password cannot be blank")
                String newPassword)
        implements Serializable {}
