package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.yubico.webauthn.data.ByteArray;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.AccountType;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapperTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void toDtoWithNull() {
        assertEquals(null, userMapper.toDTO(null));
    }

    @Test
    public void testUpdateEntity() {
        User user =
                new User(
                        1L,
                        "testFirstName",
                        "testLastName",
                        "testUsername",
                        "testPassword123!",
                        "testEmail@test.com",
                        null,
                        0L,
                        BigDecimal.ZERO,
                        new UserConfig(Role.USER, null),
                        null,
                        null,
                        new ByteArray(new byte[0]));

        UserUpdateDTO userUpdateDTO =
                new UserUpdateDTO(
                        "name",
                        "name",
                        "Test123!",
                        "username",
                        "test@test.com",
                        new AccountUpdateDTO(AccountType.SAVING, 11111111111L, BigDecimal.ZERO),
                        new AccountUpdateDTO(AccountType.SPENDING, 11111111111L, BigDecimal.ZERO));

        String password = passwordEncoder.encode("Test123!");
        userMapper.updateEntity(user, userUpdateDTO, password);
        assertEquals("name", user.getFirstName());
        assertEquals("name", user.getLastName());
        assertEquals("test@test.com", user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testUpdateEntityWithNull() {
        User user =
                new User(
                        1L,
                        "testFirstName",
                        "testLastName",
                        "testUsername",
                        "testPassword123!",
                        "testEmail@test.com",
                        null,
                        0L,
                        BigDecimal.ZERO,
                        new UserConfig(Role.USER, null),
                        new Account(),
                        new Account(),
                        new ByteArray(new byte[0]));

        userMapper.updateEntity(user, null, null);
        assertEquals("testFirstName", user.getFirstName());
        assertEquals("testLastName", user.getLastName());
        assertEquals("testUsername", user.getUsername());
        assertEquals("testPassword123!", user.getPassword());
        assertEquals("testEmail@test.com", user.getEmail());

        UserUpdateDTO userUpdateDTO =
                new UserUpdateDTO(
                        null,
                        null,
                        null,
                        null,
                        null,
                        new AccountUpdateDTO(AccountType.SAVING, 11111111111L, BigDecimal.ZERO),
                        new AccountUpdateDTO(AccountType.SPENDING, 11111111111L, BigDecimal.ZERO));

        userMapper.updateEntity(user, userUpdateDTO, null);
        assertEquals("testFirstName", user.getFirstName());
        assertEquals("testLastName", user.getLastName());
        assertEquals("testUsername", user.getUsername());
        assertEquals("testPassword123!", user.getPassword());
        assertEquals("testEmail@test.com", user.getEmail());
    }

    @Test
    public void testStreakMappingWithNull() {
        assertEquals(null, userMapper.toStreakResponse(null, null));
    }
}
