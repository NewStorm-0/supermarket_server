package com.newstorm.common;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class HmacUtils {
    private static final byte[] key = new byte[]{-56, 64, 37, 0, 80, -58, 125, -64, 84,
            -65, -91, -71, 18, 37, -1, -59, 124, -128, 13, -91, 33, -4, 120, 12, -44,
            -99, -39, 127, -34, 92, 91, -42};

    public static String encrypt(String raw) throws Exception {
        SecretKey key = new SecretKeySpec(HmacUtils.key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        mac.update(raw.getBytes(StandardCharsets.UTF_8));
        byte[] result = mac.doFinal();
        return new BigInteger(1, result).toString(16);
    }
}
