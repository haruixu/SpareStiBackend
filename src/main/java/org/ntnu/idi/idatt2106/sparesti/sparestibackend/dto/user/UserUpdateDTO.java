package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpdateDTO(
        String firstName,
        String lastName,
        String password,
        @Email(message = "Invalid email format") String email,
        AccountUpdateDTO spendingAccount,
        AccountUpdateDTO savingAccount)
        implements Serializable {}
