package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

public class RegisterMapperTest {

    @Test
    public void toEntity() {
        RegisterRequest request =
                new RegisterRequest(
                        "firstName", "lastName", "username", "password", "testEmail@mail.com");

        User user = RegisterMapper.INSTANCE.toEntity(request, Role.USER, "encodedPassword");

        assertEquals(request.getFirstName(), user.getFirstName());
        assertEquals(request.getLastName(), user.getLastName());
        assertEquals(request.getUsername(), user.getUsername());
        assertNotEquals(request.getPassword(), user.getPassword());
        assertEquals(request.getEmail(), user.getEmail());
        assertEquals(new UserConfig(Role.USER, null), user.getUserConfig());
    }

    @Test
    public void toLoginRegisterResponse() {
        User user = User.builder().id(1L).build();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        LoginRegisterResponse response =
                RegisterMapper.INSTANCE.toDTO(user, accessToken, refreshToken);

        assertEquals(user.getId(), response.getUserId());
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }
}
