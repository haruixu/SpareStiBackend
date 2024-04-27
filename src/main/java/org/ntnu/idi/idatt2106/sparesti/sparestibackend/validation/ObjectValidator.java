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

@Component
@Primary
public class ObjectValidator<T> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    protected final Validator validator = validatorFactory.getValidator();

    public void validate(T object) {
        checkConstraints(object);
    }

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
