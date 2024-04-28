package org.ntnu.idi.idatt2106.sparesti.sparestibackend.util;

import com.yubico.webauthn.data.ByteArray;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtil {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final int ROUNDING_SCALE = 3;
    private static final SecureRandom random = new SecureRandom();

    public static ByteArray generateRandom(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new ByteArray(bytes);
    }

    public static BigDecimal percent(BigDecimal quot, BigDecimal divisor) {
        return quot.multiply(ONE_HUNDRED).divide(divisor, ROUNDING_SCALE, ROUNDING_MODE);
    }
}
