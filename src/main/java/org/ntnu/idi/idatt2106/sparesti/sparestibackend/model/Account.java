package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

/**
 * Represents an embeddable account entity within a database.
 * This class is designed to be used as part of a larger entity,
 * and is not a standalone table.
 * The {@code Account} class holds
 * all necessary account information typically required for financial transactions.
 * <p>
 * This class uses Lombok to generate boilerplate code such as getter/setters and
 * toString() methods which aids in keeping the codebase clean and concise.
 *
 * @author Y.A Marouga
 */
@Data
@Embeddable
public class Account {

    /**
     * The account number, uniquely identifying this account within its context.
     * This field is immutable once set initially (non-updatable) and cannot be null.
     */
    @NotNull
    @Column(updatable = false, nullable = false)
    private String accNumber;

    /**
     * The current balance of the account.
     * This field is initialized to 0.00 and cannot be null.
     * It holds the balance of the account.
     */
    @ColumnDefault("0.00")
    @Column(nullable = false)
    @NotNull
    private BigDecimal balance;
}
