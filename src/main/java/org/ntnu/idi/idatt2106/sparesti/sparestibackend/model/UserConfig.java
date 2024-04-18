package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

@Embeddable
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserConfig {
    private Motivation motivation;
    private Role role;
    private Experience experience;
}
