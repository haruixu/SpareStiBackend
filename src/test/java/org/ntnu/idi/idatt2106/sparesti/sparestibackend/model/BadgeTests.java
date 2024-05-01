package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BadgeTests {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNotNullConstraints() {
        Badge badge = new Badge();
        Set<ConstraintViolation<Badge>> violations = validator.validate(badge);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("title")
                                                && v.getMessage().contains("must not be null")),
                "Title must not be null validation should fail when not set");
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("description")
                                                && v.getMessage().contains("must not be null")),
                "Description must not be null validation should fail when not set");
    }

    @Test
    void testValidBadge() {
        Badge badge = new Badge();
        badge.setTitle("Achiever");
        badge.setDescription("Awarded for completing 10 challenges");
        Set<ConstraintViolation<Badge>> violations = validator.validate(badge);
        assertTrue(
                violations.isEmpty(),
                "There should be no violations when title and description are properly set");
    }
}
