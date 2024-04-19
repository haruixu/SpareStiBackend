package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;

import java.util.HashSet;
import java.util.Set;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "EXPERIENCE", "MOTIVATION"})},
        name = "CHALLENGE_CONFIG")
public class ChallengeConfig {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Experience experience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Motivation motivation;

    @ElementCollection
    @CollectionTable(name = "CHALLENGETYPE_CONFIG")
    Set<ChallengeTypeConfig> challengeTypeConfigs = new HashSet<>();
}
