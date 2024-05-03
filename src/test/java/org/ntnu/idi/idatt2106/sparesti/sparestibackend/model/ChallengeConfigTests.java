package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;

class ChallengeConfigTests {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNotNullConstraints() {
        ChallengeConfig challengeConfig = new ChallengeConfig();
        Set<ConstraintViolation<ChallengeConfig>> violations = validator.validate(challengeConfig);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("experience")
                                                && v.getMessage().contains("must not be null")),
                "Experience must not be null validation failed");
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("motivation")
                                                && v.getMessage().contains("must not be null")),
                "Motivation must not be null validation failed");
    }

    @Test
    void testValidChallengeConfig() {
        ChallengeConfig challengeConfig =
                new ChallengeConfig(Experience.HIGH, Motivation.HIGH, new HashSet<>());
        Set<ConstraintViolation<ChallengeConfig>> violations = validator.validate(challengeConfig);
        assertTrue(
                violations.isEmpty(),
                "There should be no violations when experience and motivation are properly set");
    }
}
