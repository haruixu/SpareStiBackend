package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.account.AccountType;

public record AccountDTO(
        @NotNull(message = "Account type cannot be null") AccountType accountType,
        @NotNull(message = "Account number cannot be null")
                @Positive(message = "Account number must be positive")
                Long accNumber,
        @NotNull(message = "Balance cannot be null") @Positive(message = "Balance must be positive")
                BigDecimal balance)
        implements Serializable {}
