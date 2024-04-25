package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "TYPE"})},
        name = "CHALLENGETYPE_CONFIG")
public class ChallengeTypeConfig {

    @Column(nullable = false, name = "TYPE", updatable = false)
    @Setter(AccessLevel.NONE)
    @Size(max = 20, message = "Type can have max 20 characters")
    private String type;

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
