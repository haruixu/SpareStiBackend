package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Embeddable
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "TITLE"})})
public class Goal implements Comparable<Goal> {

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false)
    @NotNull
    @ColumnDefault("0.00")
    private BigDecimal saved;

    @Column(nullable = false)
    @NotNull
    private BigDecimal target;

    @Column(nullable = false)
    @NotNull
    private String description;

    @Column(nullable = false, name = "ORDER")
    private Long order;

    @CreationTimestamp private LocalDateTime createdOn;

    @Transient private double completion;

    @Override
    public int compareTo(Goal goal) {
        return Long.compare(this.order, goal.getOrder());
    }
}
