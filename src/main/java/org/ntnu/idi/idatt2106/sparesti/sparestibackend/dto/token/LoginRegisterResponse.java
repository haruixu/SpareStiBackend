package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO used returning JWT tokens upon successful login and register
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoginRegisterResponse {
    @NotNull Long id;
    @NotNull @NotBlank @NotEmpty String accessToken;
    @NotNull @NotBlank @NotEmpty private String refreshToken;
}
