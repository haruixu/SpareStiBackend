package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
@Data
public class Goal {

  @Column(nullable = false)
  @NotNull
  private String title;

  @Column(nullable = false)
  @NotNull
  @ColumnDefault("0.00")
  private BigDecimal saved;

  @Column(nullable = false)
  @NotNull
  private BigDecimal goal;

  @Column(nullable = false)
  @NotNull
  private String description;

  @CreationTimestamp
  private LocalDateTime createdOn;

  @Transient
  private double completion;
}
