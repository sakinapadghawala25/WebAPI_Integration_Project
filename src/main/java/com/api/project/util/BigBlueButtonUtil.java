package com.api.project.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BigBlueButtonUtil {
    public static String calculateChecksum(String data, String sharedSecret) {
        try {
            // Concatenate data with the shared secret
            String input = data + sharedSecret;

            // Create SHA-1 MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // Compute the digest
            byte[] digest = md.digest(input.getBytes());

            // Convert byte array to hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }
}
