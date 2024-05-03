package org.ntnu.idi.idatt2106.sparesti.sparestibackend.util;

import com.yubico.webauthn.data.ByteArray;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;

/**
 * Util class for method used in application
 */
@Component
public class ApplicationUtil {

    /**
     * Static variable for one hundred
     */
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /**
     * Static variable for rounding mode up
     */
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Static variable for rounding weight (3 decimals)
     */
    private static final int ROUNDING_SCALE = 3;

    /**
     * Static variable for random
     */
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates random byte array.
     * @param length Specified length of byte array
     * @return Random byte array
     */
    public static ByteArray generateRandom(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new ByteArray(bytes);
    }

    /**
     * Calculates percentage given two input numbers
     * @param quot Divident
     * @param divisor Divisor
     * @return Percentage value
     */
    public static BigDecimal percent(BigDecimal quot, BigDecimal divisor) {
        return quot.multiply(ONE_HUNDRED).divide(divisor, ROUNDING_SCALE, ROUNDING_MODE);
    }
}
