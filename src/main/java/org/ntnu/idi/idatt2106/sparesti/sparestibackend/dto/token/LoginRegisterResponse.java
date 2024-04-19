package org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LoginRegisterResponse {
    @NotNull Long id;
    @NotNull @NotBlank @NotEmpty String accessToken;
    @NotNull @NotBlank @NotEmpty private String refreshToken;
}
