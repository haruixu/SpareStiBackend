package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents different types of challenges and their associated monetary values for each consumption.
 */
@Getter
@AllArgsConstructor
public enum ChallengeType {
    CIGAR(20),
    CLOTHING(200),
    COFFEE(20),
    SNUFF(20),
    SNACKS(20),
    TRANSPORTATION(20);

    private final int val;
}
