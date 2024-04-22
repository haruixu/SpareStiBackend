package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.*;

/**
 * DTO used returning JWT tokens upon successful login and register
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Value
public class LoginRegisterResponse implements Serializable {
    @NotNull @NotBlank @NotEmpty String accessToken;
    @NotNull @NotBlank @NotEmpty String refreshToken;
}
