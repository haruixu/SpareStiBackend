package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Embeddable
@Data
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

  @CreationTimestamp
  private LocalDateTime createdOn;

  @Transient
  private double completion;
}
