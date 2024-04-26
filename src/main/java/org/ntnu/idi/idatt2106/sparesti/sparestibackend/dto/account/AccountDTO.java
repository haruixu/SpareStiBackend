package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.AccountType;

public record AccountDTO(
        @NotNull(message = "Account type cannot be null") AccountType accountType,
        @NotNull(message = "Account number cannot be null")
                @PositiveOrZero(message = "Account number must be positive or zero")
                Long accNumber,
        @NotNull(message = "Balance cannot be null")
                @PositiveOrZero(message = "Balance must be positive or zero")
                BigDecimal balance)
        implements Serializable {}
