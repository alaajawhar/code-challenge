package com.fintech.cms.service.impl;

import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class EncryptionService {
    
    private static final String KEY = "MySecretKey12345";
    
    public String encrypt(String plainText) {
        try {
            byte[] encrypted = xorWithKey(plainText.getBytes(), KEY.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during encryption", e);
        }
    }
    
    public String decrypt(String encryptedText) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = xorWithKey(decoded, KEY.getBytes());
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during decryption", e);
        }
    }
    
    private byte[] xorWithKey(byte[] data, byte[] key) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ key[i % key.length]);
        }
        return result;
    }
}