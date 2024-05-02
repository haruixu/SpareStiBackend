package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

/**
 * Represents embedded configuration details for a user. This class includes user-specific settings
 * such as their role and configuration for challenges, enabling customized user experiences and permissions.
 *
 * @author Y.A Marouga
 * @Embeddable Marks this class as embeddable, meaning it can be embedded into other entities rather than existing independently.
 * @Getter Lombok annotation to generate getters for all fields.
 * @AllArgsConstructor Generates a constructor with one parameter for each field.
 * @NoArgsConstructor Generates a protected no-argument constructor, suitable for JPA.
 * @Table Specifies the table name for this embedded class and includes constraints on the uniqueness of role per user.
 */
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "ROLE"})},
        name = "USER_CONFIG")
public class UserConfig {

    /**
     * The role of the user, which defines their permissions and capabilities within the system.
     * The role is stored as an enumerated type and is required.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Role role;

    /**
     * Configuration for challenges associated with the user. This setting allows for personalized
     * challenge management based on user preferences and behaviors.
     */
    @Setter private ChallengeConfig challengeConfig;
}
