package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * DTO used when requesting for renewing access token
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@AllArgsConstructor
@Data
public class AccessTokenRequest {
    @NonNull @NotBlank @NotEmpty private String refreshToken;
}
