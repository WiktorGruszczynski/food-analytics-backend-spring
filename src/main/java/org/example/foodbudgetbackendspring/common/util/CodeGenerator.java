package org.example.foodbudgetbackendspring.common.util;

import java.security.SecureRandom;

public class CodeGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOptCode() {
        int code = 100_000 + random.nextInt(900_000);
        return String.valueOf(code);
    }
}
