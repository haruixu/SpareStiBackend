package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpdateDTO(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String username,
        @NotNull String password,
        @NotNull @Email String email,
        @NotNull AccountUpdateDTO spendingAccount,
        @NotNull AccountUpdateDTO savingAccount)
        implements Serializable {}
