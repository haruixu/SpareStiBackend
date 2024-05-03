package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User}
 * @param firstName First name
 * @param lastName last name
 * @param password password
 * @param username username
 * @param email email
 * @param spendingAccount spending account
 * @param savingAccount saving account
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpdateDTO(
        String firstName,
        String lastName,
        String password,
        String username,
        @Email(message = "Ugyldig mail") String email,
        AccountUpdateDTO spendingAccount,
        AccountUpdateDTO savingAccount)
        implements Serializable {}
