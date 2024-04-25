package org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ObjectNotValidException extends RuntimeException {

    private final Set<String> errorMessages;
}
