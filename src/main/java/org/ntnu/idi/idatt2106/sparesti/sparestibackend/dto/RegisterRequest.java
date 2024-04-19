package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
}
