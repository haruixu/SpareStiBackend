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

class ChallengeTypeConfigTests {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNotNullConstraints() {
        ChallengeTypeConfig config = new ChallengeTypeConfig();
        Set<ConstraintViolation<ChallengeTypeConfig>> violations = validator.validate(config);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("generalAmount")
                                                && v.getMessage().contains("must not be null")),
                "General amount must not be null validation failed");
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("specificAmount")
                                                && v.getMessage().contains("must not be null")),
                "Specific amount must not be null validation failed");
    }

    @Test
    void testSizeConstraint() {
        ChallengeTypeConfig config =
                new ChallengeTypeConfig(
                        "ThisTypeNameIsDefinitelyTooLongToBeAllowed",
                        new BigDecimal("100.00"),
                        new BigDecimal("50.00"));
        Set<ConstraintViolation<ChallengeTypeConfig>> violations = validator.validate(config);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("type")
                                                && v.getMessage()
                                                        .contains(
                                                                "Type can have max 20 characters")),
                "Type length validation should fail when exceeded");
    }

    @Test
    void testValidChallengeTypeConfig() {
        ChallengeTypeConfig config =
                new ChallengeTypeConfig("Food", new BigDecimal("100.00"), new BigDecimal("50.00"));
        Set<ConstraintViolation<ChallengeTypeConfig>> violations = validator.validate(config);
        assertTrue(
                violations.isEmpty(),
                "There should be no violations for a valid configuration setup");
    }

    @Test
    void testEquals() {
        ChallengeTypeConfig config =
                new ChallengeTypeConfig("Food", new BigDecimal("100.00"), new BigDecimal("50.00"));

        ChallengeTypeConfig config1 =
                new ChallengeTypeConfig("Food", new BigDecimal("100.00"), new BigDecimal("50.00"));

        assertEquals(config, config1);
        assertEquals(config.hashCode(), config1.hashCode());

        ChallengeTypeConfig config2 =
                new ChallengeTypeConfig("Food", new BigDecimal("200.00"), new BigDecimal("50.00"));

        assertNotEquals(config, config2);
        assertNotEquals(config.hashCode(), config1.hashCode());

        assertNotEquals(null, config2);
        assertNotEquals(config2, null);
    }
}
