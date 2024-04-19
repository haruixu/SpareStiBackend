package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO used when returning access token
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
public class AccessTokenResponse {
    private String accessToken;
}
