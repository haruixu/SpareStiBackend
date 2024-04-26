package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Classifies a user's motivation to save into 5 distinct levels
 *
 * @author Yasin A.M
 * @version 1.0
 * @since 17.4.24
 */
@Getter
@RequiredArgsConstructor
public enum Motivation {
    /**
     * Very high motivation
     */
    VERY_HIGH(0.8),

    /**
     * High motivation
     */
    HIGH(0.65),

    /**
     * Average motivation
     */
    MEDIUM(0.5),

    /**
     * Low motivation
     */
    LOW(0.35),

    /**
     * Very low motivation
     */
    VERY_LOW(0.25);

    private final double val;
}
