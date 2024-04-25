package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.AccountType;

public record AccountUpdateDTO(
        @NotNull AccountType accountType, @Positive Long accNumber, @Positive BigDecimal balance)
        implements Serializable {}
