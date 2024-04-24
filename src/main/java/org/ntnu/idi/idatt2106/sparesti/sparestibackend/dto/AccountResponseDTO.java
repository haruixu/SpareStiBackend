package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account;

public record AccountResponseDTO(@NotNull Account savingAccount, @NotNull Account spendingAccount)
        implements Serializable {}
