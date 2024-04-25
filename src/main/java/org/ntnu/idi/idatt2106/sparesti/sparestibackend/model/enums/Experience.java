package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums;

import lombok.Getter;

@Getter
public enum Experience {
    VERY_HIGH(5),
    HIGH(4),
    MEDIUM(3),
    LOW(2),
    VERY_LOW(1);

    private final int val;

    Experience(int val) {
        this.val = val;
    }
}
