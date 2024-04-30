package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChallengeTests {

    private static Validator validator;

    private static User user;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user =
                User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .username("johndoe")
                        .password("password123")
                        .email("invalidemail")
                        .build();
    }

    @Test
    void testChallengeNotNullConstraints() {
        Challenge challenge = new Challenge();
        Set<ConstraintViolation<Challenge>> violations = validator.validate(challenge);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("title")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("saved")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("target")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("perPurchase")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("user")
                                                && v.getMessage().contains("must not be null")));
    }

    @Test
    void testChallengeFieldConstraints() {

        Challenge challenge =
                new Challenge(
                        2L,
                        "Very Long Title Exceeding Twenty Characters",
                        new BigDecimal("-1.00"),
                        new BigDecimal("1000.00"),
                        new BigDecimal("0.00"),
                        "This is a valid description with less than two hundred and eighty"
                                + " characters.",
                        null,
                        null,
                        null,
                        "coffe",
                        user,
                        new BigDecimal(1));

        Set<ConstraintViolation<Challenge>> violations = validator.validate(challenge);
        assertEquals(3, violations.size());
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v -> v.getMessage().contains("Title can have max 20 characters")));

        /*
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must be greater than or equal to 0")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must be greater than 0")));
        */
    }

    @Test
    void testValidChallenge() {
        Challenge challenge =
                new Challenge(
                        2L,
                        "Valid Title",
                        new BigDecimal("50.00"),
                        new BigDecimal("100.00"),
                        new BigDecimal("5.00"),
                        "Valid Description",
                        null,
                        null,
                        null,
                        "coffe",
                        user,
                        new BigDecimal(1));

        Set<ConstraintViolation<Challenge>> violations = validator.validate(challenge);
        System.out.println(violations);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEquals() {
        Challenge challenge =
                new Challenge(
                        2L,
                        "Valid Title",
                        new BigDecimal("50.00"),
                        new BigDecimal("100.00"),
                        new BigDecimal("5.00"),
                        "Valid Description",
                        null,
                        null,
                        null,
                        "coffe",
                        user,
                        new BigDecimal(1));

        Challenge challenge1 =
                new Challenge(
                        2L,
                        "Valid Title",
                        new BigDecimal("50.00"),
                        new BigDecimal("100.00"),
                        new BigDecimal("5.00"),
                        "Valid Description",
                        null,
                        null,
                        null,
                        "coffe",
                        user,
                        new BigDecimal(1));

        assertEquals(challenge, challenge1);

        challenge1.setTitle("HEllo");

        assertNotEquals(challenge, challenge1);
    }
}
