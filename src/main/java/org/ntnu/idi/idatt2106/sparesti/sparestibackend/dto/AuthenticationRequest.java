package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Value
public class AuthenticationRequest {
    @NonNull @NotBlank @NotEmpty String username;
    @NonNull @NotBlank @NotEmpty String password;
}
