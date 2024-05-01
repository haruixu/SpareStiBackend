package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChangePasswordRequestTests {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNotNullConstraints() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(request);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("id")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("userID")
                                                && v.getMessage().contains("must not be null")));
    }

    @Test
    void testValidRequest() {
        ChangePasswordRequest request =
                ChangePasswordRequest.builder().id("REQ123456").userID(1001L).build();
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "There should be no violations for a valid request");
    }

    @Test
    void testCreationTimestamp() {
        ChangePasswordRequest request =
                ChangePasswordRequest.builder().id("REQ123456").userID(1001L).build();
        assertNull(
                request.getTime(),
                "Time should be null when not explicitly set due to @CreationTimestamp not being"
                        + " applied outside of a managed context");
    }
}
