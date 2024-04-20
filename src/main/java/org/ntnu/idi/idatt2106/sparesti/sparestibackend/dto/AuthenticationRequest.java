package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Value
public class AuthenticationRequest implements Serializable {
    @NotNull @NotBlank @NotEmpty String username;
    @NotNull @NotBlank @NotEmpty String password;
}
