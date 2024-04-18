package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.ChallengeType;

@Embeddable
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "TYPE"})},
        name = "CHALLENGE_CONFIG")
@EqualsAndHashCode
public class ChallengeConfig {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "TYPE")
    private ChallengeType type;

    @Column(nullable = false)
    private BigDecimal baseline;
}
