package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequestRequest(@NotNull @NotEmpty @NotBlank String email) {}
