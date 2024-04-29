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

class UserConfigTests {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNotNullConstraints() {
        UserConfig userConfig = new UserConfig();
        Set<ConstraintViolation<UserConfig>> violations = validator.validate(userConfig);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("role")
                                                && v.getMessage().contains("must not be null")),
                "Role must not be null validation failed");
    }

    @Test
    void testValidUserConfig() {
        UserConfig userConfig = new UserConfig(Role.ADMIN, null);
        Set<ConstraintViolation<UserConfig>> violations = validator.validate(userConfig);
        assertTrue(violations.isEmpty(), "There should be no violations when role is properly set");
    }

    @Test
    void testChallengeConfigAssociation() {
        ChallengeConfig challengeConfig = new ChallengeConfig();
        UserConfig userConfig = new UserConfig(Role.USER, challengeConfig);
        assertEquals(
                challengeConfig,
                userConfig.getChallengeConfig(),
                "ChallengeConfig should be correctly associated with UserConfig");
    }
}
