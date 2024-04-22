package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Experience {
    VERY_HIGH(5),
    HIGH(4),
    MIDDLE(3),
    LOW(2),
    VERY_LOW(1);

    private final int val;
}
