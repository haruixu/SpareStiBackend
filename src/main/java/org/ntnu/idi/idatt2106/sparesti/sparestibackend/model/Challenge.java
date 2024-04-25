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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CHALLENGE")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Size(max = 20, message = "Title can have max 20 characters")
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
    @Positive
    private BigDecimal perPurchase;

    @Size(max = 280, message = "Description can have max 280 characters")
    private String description;

    @Column(nullable = false, name = "CREATION", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Setter(AccessLevel.NONE)
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
    @Setter(AccessLevel.NONE)
    private User user;

    @Transient private BigDecimal completion;
}
