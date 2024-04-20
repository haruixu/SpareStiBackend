package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import lombok.*;

/**
 * DTO used when returning access token
 *
 * @author Harry L.X & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Value
public class AccessTokenResponse {
    private String accessToken;
}
