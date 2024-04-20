package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChangePasswordRequest {

  @Id
  @NotNull
  @Column(unique = true, nullable = false)
  private String id;

  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private LocalDateTime time;

  @NotNull
  @Column(unique = true, nullable = false)
  private Long userID;
}
