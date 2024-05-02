package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.*;

/**
 * Embedded class that represents configuration settings specific to a type of challenge within the system.
 * This configuration includes financial thresholds associated with a challenge type, which are used to tailor
 * the challenge parameters to the user's spending habits and goals.
 *
 * <p>This class is designed to be embedded within another entity and is not a standalone table. It includes
 * unique constraints to ensure that each user can have only one configuration per challenge type.</p>
 *
 * @author Y.A Marouga
 * @Embeddable Marks this class as embeddable, meaning it can be embedded into other entities.
 * @AllArgsConstructor Lombok annotation to generate a constructor with one parameter for each field in the class.
 * @NoArgsConstructor Generates a protected no-argument constructor for JPA use.
 * @Data Generates getters, setters, and other typical methods like equals, hashCode from the fields of the class.
 * @Table Specifies the table name and unique constraints for the fields when they are persisted as part of another entity.
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "TYPE"})},
        name = "CHALLENGETYPE_CONFIG")
public class ChallengeTypeConfig {

    /**
     * The type of challenge this configuration pertains to, which cannot be updated once set.
     * It is limited to 20 characters to ensure concise and relevant descriptors.
     */
    @Column(nullable = false, name = "TYPE", updatable = false)
    @Setter(AccessLevel.NONE)
    @Size(max = 20, message = "Type can have max 20 characters")
    private String type;

    /**
     * The average amount a user spends on this challenge type in a typical week.
     * This field helps in setting realistic and personalized challenge goals.
     */
    @Column(nullable = false)
    @NotNull
    private BigDecimal generalAmount;

    /**
     * The amount a user typically spends each time they engage with this challenge type.
     * This specific amount helps refine the challenge parameters for budgeting and saving.
     */
    @Column(nullable = false)
    @NotNull
    private BigDecimal specificAmount;
}
