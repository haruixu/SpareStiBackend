package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.*;

/**
 * DTO used when registering a new user
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Value
public class RegisterRequest implements Serializable {
    @NonNull @NotBlank @NotEmpty String firstName;
    @NonNull @NotBlank @NotEmpty String lastName;
    @NonNull @NotBlank @NotEmpty String username;
    @NonNull @NotBlank @NotEmpty String password;
    @NonNull @NotBlank @NotEmpty String email;
}
