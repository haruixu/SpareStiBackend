package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record ChangePasswordRequestRequest(
        @NotNull(message = "Email cannot be null") @Email(message = "Invalid email format")
                String email)
        implements Serializable {}
