package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.AccountType;

/**
 * DTO for {@link org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account}
 * @param accountType Type of account
 * @param accNumber Account number
 * @param balance Account balance
 */
public record AccountUpdateDTO(
        @NotNull(message = "Account type cannot be null") AccountType accountType,
        @PositiveOrZero(message = "Account number must be positive or zero") Long accNumber,
        @PositiveOrZero(message = "Balance must be positive or zero") BigDecimal balance)
        implements Serializable {}
