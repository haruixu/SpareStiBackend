package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
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
    private Motivation motivation;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Experience experience;
}
