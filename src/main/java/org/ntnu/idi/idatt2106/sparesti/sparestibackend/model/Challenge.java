package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Embeddable
@Data
@EqualsAndHashCode
@Entity
@Table(name = "CHALLENGE")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false)
    @NotNull
    @ColumnDefault("0.00")
    @PositiveOrZero
    private BigDecimal saved;

    @Column(nullable = false)
    @NotNull
    @Positive
    private BigDecimal target;

    @Column(nullable = false)
    @NotNull
    private String description;

    @Column(nullable = false, name = "CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private ZonedDateTime createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime completedOn;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime due;

    @Column(name = "TYPE")
    private String type;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Transient private BigDecimal completion;
}
