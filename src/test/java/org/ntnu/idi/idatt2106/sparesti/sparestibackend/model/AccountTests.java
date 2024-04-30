package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AccountTests {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAccountNotNullConstraints() {
        Account account = new Account();
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("accNumber")
                                                && v.getMessage().contains("must not be null")));
        assertTrue(
                violations.stream()
                        .anyMatch(
                                v ->
                                        v.getPropertyPath().toString().equals("balance")
                                                && v.getMessage().contains("must not be null")));
    }

    @Test
    void testAccountValidFields() {
        Account account = new Account();
        account.setAccNumber("1234567890");
        account.setBalance(new BigDecimal("100.00"));
        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testAccountInitialBalance() {
        Account account = new Account();
        account.setAccNumber("1234567890");
        assertNull(
                account.getBalance(),
                "Initial balance should be null if not set due to no automatic application of"
                        + " @ColumnDefault by Hibernate in this context");
    }

    @Test
    void testAccountBalanceUpdates() {
        Account account = new Account();
        account.setAccNumber("1234567890");
        account.setBalance(new BigDecimal("50.00"));
        assertEquals(
                0,
                account.getBalance().compareTo(new BigDecimal("50.00")),
                "Balance should be exactly 50.00");
    }

    @Test
    void testEquals() {
        Account account = new Account();
        account.setAccNumber("1234567890");
        account.setBalance(new BigDecimal("100.00"));

        Account account1 = new Account();
        account1.setAccNumber("1234567890");
        account1.setBalance(new BigDecimal("100.00"));

        assertEquals(account, account1);

        account1.setAccNumber("111111111");
        assertNotEquals(account, account1);
    }
}
