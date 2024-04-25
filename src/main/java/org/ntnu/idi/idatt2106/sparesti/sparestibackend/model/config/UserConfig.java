package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "ROLE"})},
        name = "USER_CONFIG")
public class UserConfig {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Role role;

    @Setter private ChallengeConfig challengeConfig;
}
