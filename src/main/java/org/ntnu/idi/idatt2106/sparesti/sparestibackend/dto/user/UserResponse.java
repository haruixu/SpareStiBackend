package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.BadgeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountDTO;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User}
 * @param id Id of user
 * @param firstName First name
 * @param lastName Last name
 * @param username username
 * @param email Email
 * @param spendingAccount Spending account
 * @param savingAccount saving account
 * @param savedAmount Saved money
 * @param badges User badges
 * @param hasPasskey Boolean for whether user has biometric login
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(
        Long id,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String username,
        @NotNull @Email String email,
        @NotNull AccountDTO spendingAccount,
        @NotNull AccountDTO savingAccount,
        @NotNull BigDecimal savedAmount,
        @NotNull Set<BadgeDTO> badges,
        @NotNull Boolean hasPasskey)
        implements Serializable {}
