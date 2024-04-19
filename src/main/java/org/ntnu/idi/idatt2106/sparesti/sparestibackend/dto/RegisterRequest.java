package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO used when registering a new user
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest implements Serializable {
    @NonNull @NotBlank @NotEmpty private String firstName;
    @NonNull @NotBlank @NotEmpty private String lastName;
    @NonNull @NotBlank @NotEmpty private String username;
    @NonNull @NotBlank @NotEmpty private String password;
    @NonNull @NotBlank @NotEmpty private String email;
}
