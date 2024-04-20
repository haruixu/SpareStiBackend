package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Embeddable
@Data
@EqualsAndHashCode
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "TITLE"})})
public class Goal implements Comparable<Goal> {

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false)
    @NotNull
    @Positive
    @ColumnDefault("0.00")
    private BigDecimal saved;

    @Column(nullable = false)
    @NotNull
    @Positive
    private BigDecimal target;

    @Column(nullable = false)
    @NotNull
    private String description;

    @Column(nullable = false, name = "PRIORITY")
    @NotNull
    private Long priority;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn;

    @Transient @PositiveOrZero private BigDecimal completion;

    @Override
    public int compareTo(Goal goal) {
        return Long.compare(this.priority, goal.getPriority());
    }
}
