package com.fintech.cms.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EncryptionServiceTest {

    @InjectMocks
    private EncryptionService encryptionService;

    private String testCardNumber;

    @BeforeEach
    void setUp() {
        testCardNumber = "1234567812345678";
    }

    @Test
    void encrypt_Success() {
        String encrypted = encryptionService.encrypt(testCardNumber);

        assertNotNull(encrypted);
        assertNotEquals(testCardNumber, encrypted);
    }

    @Test
    void decrypt_Success() {
        String encrypted = encryptionService.encrypt(testCardNumber);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(testCardNumber, decrypted);
    }

    @Test
    void encryptDecrypt_RoundTrip() {
        String original = "9876543210987654";
        String encrypted = encryptionService.encrypt(original);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(original, decrypted);
    }

    @Test
    void encrypt_NullInput() {
        assertThrows(Exception.class, () -> encryptionService.encrypt(null));
    }

    @Test
    void decrypt_NullInput() {
        assertThrows(Exception.class, () -> encryptionService.decrypt(null));
    }

    @Test
    void encrypt_EmptyString() {
        String result = encryptionService.encrypt("");
        assertNotNull(result);
    }

    @Test
    void decrypt_InvalidInput() {
        assertThrows(Exception.class, () -> encryptionService.decrypt("invalid-encrypted-data"));
    }

    @Test
    void encrypt_SameInputGivesSameOutput() {
        String encrypted1 = encryptionService.encrypt(testCardNumber);
        String encrypted2 = encryptionService.encrypt(testCardNumber);

        assertEquals(encrypted1, encrypted2);
    }

    @Test
    void encrypt_DifferentInputGivesDifferentOutput() {
        String card1 = "1111222233334444";
        String card2 = "5555666677778888";

        String encrypted1 = encryptionService.encrypt(card1);
        String encrypted2 = encryptionService.encrypt(card2);

        assertNotEquals(encrypted1, encrypted2);
    }
}