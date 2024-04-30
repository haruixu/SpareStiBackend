package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpdateDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String password,
        @Email(message = "Invalid email format") String email,

        // TODO: Possibly make this @NotNull
        AccountUpdateDTO spendingAccount,
        AccountUpdateDTO savingAccount)
        implements Serializable {}
