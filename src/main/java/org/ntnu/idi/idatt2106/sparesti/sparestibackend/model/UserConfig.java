package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserConfig {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Motivation motivation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Experience experience;

    @ElementCollection
    @CollectionTable(name = "CHALLENGE_CONFIG")
    Set<ChallengeConfig> challengeConfigs = new HashSet<>();
}
