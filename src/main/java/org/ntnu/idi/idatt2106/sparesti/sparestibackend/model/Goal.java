package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
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
    @ColumnDefault("0.00")
    private BigDecimal saved;

    @Column(nullable = false)
    @NotNull
    private BigDecimal target;

    @Column(nullable = false)
    @NotNull
    private String description;

    @Column(nullable = false, name = "PRIORITY")
    @NotNull
    private Long priority;

    @CreationTimestamp private LocalDateTime createdOn;

    @Transient private BigDecimal completion;

    @Override
    public int compareTo(Goal goal) {
        return Long.compare(this.priority, goal.getPriority());
    }
}
