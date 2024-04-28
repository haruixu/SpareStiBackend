package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Validator class for DTO objects received from HTTP-requests
 * Its generic type allows flexibility across DTO's
 *
 * @author Harry L.X
 * @version 1.0
 * @since 24.4.24
 * @param <T> The DTO type that class is validating
 */
@Component
@Primary
public class ObjectValidator<T> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    protected final Validator validator = validatorFactory.getValidator();

    /**
     * Validates the object
     * @param object Object of type T
     */
    public void validate(T object) {
        checkConstraints(object);
    }

    /**
     * Checks that no field constraints are violated in the object
     * @param object The object of type T
     */
    protected void checkConstraints(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            Set<String> errorMessages =
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.toSet());

            throw new ObjectNotValidException(errorMessages);
        }
    }
}
