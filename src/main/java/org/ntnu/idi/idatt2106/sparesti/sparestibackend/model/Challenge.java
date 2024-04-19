package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.ChallengeType;

@Embeddable
@Data
@EqualsAndHashCode
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "TITLE"})})
public class Challenge {

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

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "TYPE")
    private ChallengeType type;

    @Transient private BigDecimal completion;
}
