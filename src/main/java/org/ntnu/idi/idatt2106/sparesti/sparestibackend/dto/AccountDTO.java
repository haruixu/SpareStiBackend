package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.AccountType;

public record AccountDTO(
        @NotNull AccountType accountType,
        @NotNull @Positive Long accNumber,
        @NotNull @Positive BigDecimal balance)
        implements Serializable {}
