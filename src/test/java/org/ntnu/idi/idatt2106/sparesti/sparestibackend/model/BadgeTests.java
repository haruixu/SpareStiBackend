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

    @Test
    void testEquals() {
        Badge badge = new Badge();
        badge.setTitle("Achiever");
        badge.setDescription("Awarded for completing 10 challenges");

        Badge badge1 = new Badge();
        badge1.setTitle("Achiever");
        badge1.setDescription("Awarded for completing 10 challenges");

        assertEquals(badge, badge1);
        assertEquals(badge.hashCode(), badge1.hashCode());

        badge1.setTitle("Hello");
        assertNotEquals(badge, badge1);
        assertNotEquals(badge.hashCode(), badge1.hashCode());

        assertNotEquals(badge, null);
        assertNotEquals(null, badge);
    }
}
