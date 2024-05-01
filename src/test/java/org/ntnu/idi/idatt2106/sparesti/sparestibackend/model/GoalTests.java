package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GoalTests {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNotNullConstraints() {
        Goal goal = new Goal();
        Set<ConstraintViolation<Goal>> violations = validator.validate(goal);
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
                                        v.getPropertyPath().toString().equals("priority")
                                                && v.getMessage().contains("must not be null")));
    }

    @Test
    void testFieldConstraints() {
        Goal goal =
                new Goal(
                        null,
                        "Title Over Twenty Characters Long",
                        new BigDecimal("50"),
                        new BigDecimal("100"),
                        "Valid Description",
                        1L,
                        ZonedDateTime.now(),
                        ZonedDateTime.now(),
                        null,
                        ZonedDateTime.now(),
                        null);
        Set<ConstraintViolation<Goal>> violations = validator.validate(goal);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getMessage()
                                                .contains("Title can at most have 20 characters")));
    }

    @Test
    void testValidGoal() {
        Goal goal =
                new Goal(
                        null,
                        "Valid Title",
                        new BigDecimal("50"),
                        new BigDecimal("100"),
                        "Valid Description",
                        1L,
                        ZonedDateTime.now(),
                        ZonedDateTime.now(),
                        new BigDecimal("50"),
                        ZonedDateTime.now(),
                        new User()); // Assuming User is properly instantiated
        Set<ConstraintViolation<Goal>> violations = validator.validate(goal);
        assertTrue(violations.isEmpty(), "There should be no violations for a valid goal");
    }

    @Test
    void testCompareTo() {
        Goal goal1 =
                new Goal(
                        null,
                        "Goal 1",
                        new BigDecimal("50"),
                        new BigDecimal("100"),
                        "Description",
                        1L,
                        ZonedDateTime.now(),
                        ZonedDateTime.now(),
                        new BigDecimal("50"),
                        ZonedDateTime.now(),
                        new User());
        Goal goal2 =
                new Goal(
                        null,
                        "Goal 2",
                        new BigDecimal("60"),
                        new BigDecimal("150"),
                        "Description",
                        2L,
                        ZonedDateTime.now(),
                        ZonedDateTime.now(),
                        new BigDecimal("60"),
                        ZonedDateTime.now(),
                        new User());
        assertTrue(goal1.compareTo(goal2) < 0, "goal1 should have a higher priority than goal2");
        assertTrue(goal2.compareTo(goal1) > 0, "goal2 should have a lower priority than goal1");
    }
}
