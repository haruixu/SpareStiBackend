package org.ntnu.idi.idatt2106.sparesti.sparestibackend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtil {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    public static final RoundingMode roundingMode = RoundingMode.HALF_UP;
    private static final int scale = 3;
    public static String BINDING_RESULT_ERROR = "Fields in the body cannot be null, blank or empty";

    public static BigDecimal percent(BigDecimal quot, BigDecimal divisor) {
        return quot.multiply(ONE_HUNDRED).divide(divisor, scale, roundingMode);
    }
}
