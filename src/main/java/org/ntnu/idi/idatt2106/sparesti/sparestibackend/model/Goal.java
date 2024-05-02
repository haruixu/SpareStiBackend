package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * Represents a financial goal set by a user.
 * Goals track progress towards a financial target,
 * prioritized by the user, with a set timeframe and other descriptive details.
 * Implements {@link Comparable} to enable sorting based on priority.
 *
 * @author Y.A Marouga
 * @Entity Marks this class as a JPA entity.
 * @Data Lombok annotation to generate getters and setters, as well as other common methods like equals and hashCode.
 * @AllArgsConstructor Generates a constructor with one parameter for each field.
 * @NoArgsConstructor Generates a protected no-argument constructor required by JPA.
 * @Table Specifies the table name for this entity.
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
@Table(name = "GOAL")
public class Goal implements Comparable<Goal> {

    /**
     * The unique identifier for the goal. This field is auto-generated and not modifiable.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * The title of the goal, limited to 20 characters. This is a concise descriptor of the goal.
     */
    @Column(nullable = false)
    @NotNull
    @Size(max = 20, message = "Title can at most have 20 characters")
    private String title;

    /**
     * The amount already saved towards achieving this goal. Must be zero or positive and starts with a default of 0.00.
     */
    @Column(nullable = false)
    @NotNull
    @PositiveOrZero
    @ColumnDefault("0.00")
    private BigDecimal saved;

    /**
     * The financial target that the goal aims to reach. This amount must be positive.
     */
    @Column(nullable = false)
    @NotNull
    @Positive
    private BigDecimal target;

    /**
     * An optional detailed description of the goal, allowing up to 280 characters.
     */
    @Size(max = 280, message = "Description can at most have 280 characters")
    private String description;

    /**
     * The priority of the goal, used to sort goals when necessary. Higher numbers indicate higher priority.
     */
    @Column(nullable = false, name = "PRIORITY")
    @NotNull
    private Long priority;

    /**
     * The timestamp when the goal was created, automatically set upon creation and not updatable.
     */
    @Column(name = "CREATION", updatable = false, nullable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.NONE)
    private ZonedDateTime createdOn;

    /**
     * The due date for achieving the goal, if applicable. This is optional and can be set to manage goal deadlines.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime due;

    /**
     * A transient field that calculates the completion percentage of the goal. This field is not persisted in the database.
     */
    @Transient @PositiveOrZero private BigDecimal completion;

    /**
     * The date when the goal was completed. This field is optional and can be set once the goal is fully achieved.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime completedOn;

    /**
     * The user who owns this goal. This association is lazy loaded and merged on cascade. The user's ID is the foreign key.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonBackReference
    @Setter(AccessLevel.NONE)
    private User user;

    /**
     * Compares this goal with another to order goals based on their priority.
     * Higher priorities are considered greater in this comparison.
     *
     * @param goal The goal to compare with this instance.
     * @return a negative integer, zero, or a positive integer as this goal's priority
     *         is less than, equal to, or greater than the specified goal's priority.
     */
    @Override
    public int compareTo(Goal goal) {
        return Long.compare(this.priority, goal.getPriority());
    }
}
