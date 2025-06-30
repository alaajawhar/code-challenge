package com.fraud.rules;

import com.fraud.dto.FraudCheckRequest;
import com.fraud.dto.FraudCheckResponse;
import com.fraud.repository.TransactionHistoryRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FrequencyFraudRuleTest {

    @Test
    void shouldFlagFraudWhenFrequencyExceeded() {
        TransactionHistoryRepository repo = mock(TransactionHistoryRepository.class);
        when(repo.countByCardNumberAndTransactionTimeAfter(anyString(), any())).thenReturn(8);

        FrequencyFraudRule rule = new FrequencyFraudRule(repo);
        FraudCheckRequest req = new FraudCheckRequest("4000000000000002", new java.math.BigDecimal("100"));

        FraudCheckResponse resp = rule.check(req);

        assertTrue(resp.isFraud());
        assertTrue(resp.getReason().contains("Too many transactions"));
    }

    @Test
    void shouldPassWhenFrequencyNotExceeded() {
        TransactionHistoryRepository repo = mock(TransactionHistoryRepository.class);
        when(repo.countByCardNumberAndTransactionTimeAfter(anyString(), any())).thenReturn(2);

        FrequencyFraudRule rule = new FrequencyFraudRule(repo);
        FraudCheckRequest req = new FraudCheckRequest("4000000000000002", new java.math.BigDecimal("100"));

        FraudCheckResponse resp = rule.check(req);

        assertFalse(resp.isFraud());
        assertEquals("Frequency check passed", resp.getReason());
    }
} 