package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

class UserTests {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNotNullConstraints() {
        User user = new User();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("firstName")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("lastName")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("username")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("password")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("email")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("streak")
                                                && v.getMessage().contains("must not be null")));
    }

    @Test
    void testEmailFormat() {
        User user =
                User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .username("johndoe")
                        .password("password123")
                        .email("invalidemail")
                        .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("email")
                                                && v.getMessage()
                                                        .contains(
                                                                "must be a well-formed email"
                                                                        + " address")));
    }

    @Test
    void testValidUser() {
        User user =
                User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .username("johndoe")
                        .password("password123")
                        .email("john.doe@example.com")
                        .streak(0L)
                        .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "There should be no violations for a valid user setup");
    }

    @Test
    void testSpringSecurityMethods() {
        User user =
                User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .username("johndoe")
                        .password("password123")
                        .email("john.doe@example.com")
                        .streak(0L)
                        .userConfig(new UserConfig(Role.USER, null))
                        .build();
        assertTrue(user.isAccountNonExpired(), "Account should be non-expired");
        assertTrue(user.isAccountNonLocked(), "Account should be non-locked");
        assertTrue(user.isCredentialsNonExpired(), "Credentials should be non-expired");
        assertTrue(user.isEnabled(), "User should be enabled");
        assertNotNull(user.getAuthorities(), "Authorities should not be null");
        assertFalse(user.getAuthorities().isEmpty(), "Authorities should not be empty");
    }
}
