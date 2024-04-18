package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents different types of challenges and their associated monetary values for each consumption.
 */
@Getter
@RequiredArgsConstructor
public enum ChallengeType {
    CIGAR(20),
    CLOTHING(200),
    COFFEE(20),
    SNUFF(20),
    SNACKS(20),
    TRANSPORTATION(20);

    private final int val;
}
