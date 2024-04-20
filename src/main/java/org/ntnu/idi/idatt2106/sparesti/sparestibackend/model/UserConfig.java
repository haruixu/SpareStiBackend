package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "ROLE"})},
        name = "USER_CONFIG")
@EqualsAndHashCode
public class UserConfig {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Role role;

    private ChallengeConfig challengeConfig;
}
