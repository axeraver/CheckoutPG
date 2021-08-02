package com.jak.payz.banksimulator.payments;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomStringGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    private RandomStringGenerator() {
    }

    public static String generate() {
        byte[] buffer = new byte[20];
        random.nextBytes(buffer);
        return encoder.encodeToString(buffer);
    }

}
