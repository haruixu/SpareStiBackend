package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;

/**
 * Represents configuration preferences for challenges associated with a user within the system.
 * This embedded object stores user preferences like their experience level and motivation type,
 * which can be used to customize challenge recommendations and settings.
 *
 * <p>This class is not a standalone entity but is embedded within another entity such as a user profile.
 * It includes unique constraints to ensure that each combination of user, experience, and motivation
 * is unique within the context of its usage.</p>
 *
 * @author Y.A Marouga
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"USER_ID", "EXPERIENCE", "MOTIVATION"})
        },
        name = "CHALLENGE_CONFIG")
public class ChallengeConfig {

    /**
     * User's experience level with the system or the type of challenges being configured.
     * This field is required and is stored as a string representing the enum value.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private Experience experience;

    /**
     * User's motivation for participating in challenges, which can influence the type of challenges offered.
     * This field is required and is stored as a string representing the enum value.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private Motivation motivation;

    /**
     * A collection of {@link ChallengeTypeConfig} objects that define specific configurations for different types of challenges.
     * This set is not updatable directly through setters to maintain integrity.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CHALLENGETYPE_CONFIG")
    @Setter(AccessLevel.NONE)
    Set<ChallengeTypeConfig> challengeTypeConfigs = new HashSet<>();
}
