package com.fraud.rules;

import com.fraud.dto.FraudCheckResponse;
import com.fraud.dto.FraudCheckRequest;
import com.fraud.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FrequencyFraudRule implements FraudRule {
    private final TransactionHistoryRepository repository;
    private final int frequencyLimit = 8; // default, can be set via setter
    private final int timeIntervalHours = 1; // default, can be set via setter

    @Override
    public FraudCheckResponse check(FraudCheckRequest request) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(timeIntervalHours);
        int transactionCount = repository.countByCardNumberAndTransactionTimeAfter(request.getCardNumber(), cutoffTime);
        if (transactionCount >= frequencyLimit) {
            return new FraudCheckResponse(true, "Too many transactions (" + (transactionCount + 1) + ") in the time window");
        }
        return new FraudCheckResponse(false, "Frequency check passed");
    }
} 