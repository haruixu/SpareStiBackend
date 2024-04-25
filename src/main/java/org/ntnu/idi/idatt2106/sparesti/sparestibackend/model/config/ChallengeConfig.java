package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Table(
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"USER_ID", "EXPERIENCE", "MOTIVATION"})
        },
        name = "CHALLENGE_CONFIG")
public class ChallengeConfig {

    @Enumerated(EnumType.STRING)
    @NotNull
    private Experience experience;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Motivation motivation;

    @ElementCollection
    @CollectionTable(name = "CHALLENGETYPE_CONFIG")
    @Setter(AccessLevel.NONE)
    Set<ChallengeTypeConfig> challengeTypeConfigs = new HashSet<>();
}
