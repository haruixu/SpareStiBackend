package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GOAL")
public class Goal implements Comparable<Goal> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false)
    @NotNull
    @PositiveOrZero
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

    @Column(name = "CREATION")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime due;

    @Transient @PositiveOrZero private BigDecimal completion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Override
    public int compareTo(Goal goal) {
        return Long.compare(this.priority, goal.getPriority());
    }
}
