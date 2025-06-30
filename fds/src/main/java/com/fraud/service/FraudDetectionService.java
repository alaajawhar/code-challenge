package com.fraud.service;

import com.fraud.dto.FraudCheckRequest;
import com.fraud.dto.FraudCheckResponse;
import com.fraud.entity.TransactionHistory;
import com.fraud.repository.TransactionHistoryRepository;
import com.fraud.rules.FraudRule;
import com.fraud.rules.HistoricalRule;
import com.fraud.rules.NonHistoricalRule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudDetectionService implements IFraudDetectionService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    private final HistoricalRule historicalRule;
    private final NonHistoricalRule nonHistoricalRule;

    private List<FraudRule> fraudRules = new ArrayList<>();

    @PostConstruct
    public void initRules() {
        fraudRules.add(historicalRule);
        fraudRules.add(nonHistoricalRule);
    }

    @Override
    public FraudCheckResponse checkForFraud(FraudCheckRequest request) {
        log.info("Checking transaction for fraud: Card={}, Amount={}", request.getCardNumber(), request.getAmount());
        for (FraudRule rule : fraudRules) {
            FraudCheckResponse response = rule.check(request);
            if (response.isFraud()) {
                return response;
            }
        }
        saveTransaction(request);
        return new FraudCheckResponse(false, "Transaction passed all fraud checks");
    }

    private void saveTransaction(FraudCheckRequest request) {
        TransactionHistory history = new TransactionHistory();
        history.setCardNumber(request.getCardNumber());
        history.setAmount(request.getAmount());
        history.setTransactionTime(LocalDateTime.now());
        transactionHistoryRepository.save(history);
        log.debug("Recorded transaction for card: {}", request.getCardNumber());
    }
} 