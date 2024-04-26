package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Classifies user experience with saving to five distinct levels
 * @author Yasin A.M
 * @version 1.0
 * @since 17.4.24
 */
@Getter
@RequiredArgsConstructor
public enum Experience {

    /**
     * A lot of experience
     */
    VERY_HIGH(5),

    /**
     * More than average experience
     */
    HIGH(4),

    /**
     * Average experience
     */
    MEDIUM(3),

    /**
     * Less than average experience
     */

    LOW(2),
    /**
     * Very little experience
     */
    VERY_LOW(1);

    private final int val;
}
