package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents a saving challenge within the system.
 * A user sets a challenge to save a target amount of money either over time.
 * This entity tracks the progress and details of such challenges.
 *
 * @author Y.A Marouga
 * @Entity Marks this class as a JPA entity.
 * @Data Lombok annotation to generate getters, setters, equals, hashCode and toString methods automatically.
 * @AllArgsConstructor Lombok annotation to generate a constructor with arguments for all fields.
 * @NoArgsConstructor Generates a protected no-argument constructor to maintain JPA requirement for entities.
 * @Table Specifies the dedicated table name for this entity.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CHALLENGE")
public class Challenge {

    /**
     * Unique identifier for the challenge. This field is auto-generated and not settable.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * Title of the challenge, limited to 20 characters. This field is mandatory.
     */
    @Column(nullable = false)
    @NotNull
    @Size(max = 20, message = "Title can have max 20 characters")
    private String title;

    /**
     * The amount already saved towards completing the challenge. This value must be non-negative.
     */
    @Column(nullable = false)
    @NotNull
    @ColumnDefault("0.00")
    @PositiveOrZero(message = "Saved amount must be equal to or greater than zero")
    private BigDecimal saved;

    /**
     * The financial target for the challenge which must be a positive value.
     */
    @Column(nullable = false)
    @NotNull
    @Positive(message = "Target amount must be positive")
    private BigDecimal target;

    /**
     * Specifies how much should be saved with each qualifying purchase. This amount must be positive.
     */
    @Column(nullable = false)
    @NotNull
    @Positive(message = "Per purchase amount must be positive")
    private BigDecimal perPurchase;

    /**
     * Optional detailed description of the challenge, up to 280 characters.
     */
    @Size(max = 280, message = "Description can have max 280 characters")
    private String description;

    /**
     * The timestamp when the challenge was created. This field is automatically set when the challenge is persisted.
     */
    @Column(nullable = false, name = "CREATION", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private ZonedDateTime createdOn;

    /**
     * The timestamp when the challenge was completed. This field is optional and manually set.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime completedOn;

    /**
     * The due date for the challenge to be completed. This field is optional.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime due;

    /**
     * The type of the challenge, categorizing the challenge by some criterion. This field is optional.
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * The user who owns this challenge. This field is not updatable and is mandatory.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @Setter(AccessLevel.NONE)
    private User user;

    /**
     * A transient field calculating the completion percentage of the challenge based on the saved and target amounts.
     * This field is not persisted in the database.
     */
    @Transient private BigDecimal completion;
}
