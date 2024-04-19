package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.ChallengeType;

@Embeddable
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "TYPE"})},
        name = "CHALLENGETYPE_CONFIG")
@Data
public class ChallengeTypeConfig {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "TYPE")
    private ChallengeType type;

    /**
     * How much a user spends on this challenge type in a week
     */
    private BigDecimal generalAmount;

    /**
     * How much a user spends on this challenge type everytime it spends on this challenge type.
     */
    @Column(nullable = false)
    private BigDecimal specificAmount;
}
