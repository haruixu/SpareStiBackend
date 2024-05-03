package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Exception for goal invalid object
 *
 * @author Harry X.
 * @version 1.0
 * @since 24.4.24
 */
@RequiredArgsConstructor
@Getter
public class ObjectNotValidException extends RuntimeException {

    /**
     * Set of error messages
     */
    private final Set<String> errorMessages;
}
