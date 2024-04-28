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
        @NotNull Set<BadgeDTO> badges)
        implements Serializable {}
