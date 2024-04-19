package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO used returning JWT tokens upon successful login and register
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Builder
@Data
@AllArgsConstructor
public class LoginRegisterResponse {
    private String accessToken;
    private String refreshToken;
}
