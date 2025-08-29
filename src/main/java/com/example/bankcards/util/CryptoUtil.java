package com.example.bankcards.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {

    private static final String AES = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH_BYTES = 12;

    public static String encryptAesGcm(String plaintext, byte[] key) {
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            SecretKeySpec keySpec = new SecretKeySpec(normalizeKey(key), AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            buffer.put(iv);
            buffer.put(ciphertext);
            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new IllegalStateException("Encryption error", e);
        }
    }

    public static String decryptAesGcm(String encoded, byte[] key) {
        try {
            byte[] input = Base64.getDecoder().decode(encoded);
            ByteBuffer buffer = ByteBuffer.wrap(input);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            buffer.get(iv);
            byte[] ciphertext = new byte[buffer.remaining()];
            buffer.get(ciphertext);
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            SecretKeySpec keySpec = new SecretKeySpec(normalizeKey(key), AES);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] plain = cipher.doFinal(ciphertext);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Decryption error", e);
        }
    }

    public static String maskCardNumber(String number) {
        if (number == null || number.length() < 4) return "****";
        String last4 = number.substring(number.length() - 4);
        return "**** **** **** " + last4;
    }

    private static byte[] normalizeKey(byte[] key) {
        // Normalize to 16 bytes (AES-128) by hashing or padding
        byte[] normalized = new byte[16];
        for (int i = 0; i < normalized.length; i++) {
            normalized[i] = i < key.length ? key[i] : (byte) 0;
        }
        return normalized;
    }
}


