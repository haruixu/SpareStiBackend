package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.ChallengeType;

@Embeddable
@Data
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "TYPE"})},
        name = "CHALLENGETYPE_CONFIG")
public class ChallengeTypeConfig {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "TYPE")
    private ChallengeType type;

    /**
     * How much a user spends on this challenge type in a week
     */
    @Column(nullable = false)
    @NotNull
    private BigDecimal generalAmount;

    /**
     * How much a user spends on this challenge type everytime it spends on this challenge type.
     */
    @Column(nullable = false)
    @NotNull
    private BigDecimal specificAmount;
}
