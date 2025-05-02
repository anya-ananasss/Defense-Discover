package backend.academy.apigateway.utils;

import io.netty.util.internal.ThreadLocalRandom;

public class RandomUtils {
    public static String generateSixDigitCode() {
        int number = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(number);
    }
}
