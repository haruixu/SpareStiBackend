package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.account.Account;

public record AccountResponseDTO(@NotNull Account savingAccount, @NotNull Account spendingAccount)
        implements Serializable {}
