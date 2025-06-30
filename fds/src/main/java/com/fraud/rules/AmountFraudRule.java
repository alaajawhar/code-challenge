package com.fraud.rules;

import com.fraud.dto.FraudCheckResponse;
import com.fraud.dto.FraudCheckRequest;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AmountFraudRule implements FraudRule {
    private final KieContainer kieContainer;
    @Value("${fraud.detection.amount-limit}")
    private BigDecimal amountLimit;

    @Override
    public FraudCheckResponse check(FraudCheckRequest request) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            FraudCheckResponse response = new FraudCheckResponse();
            response.setFraud(false);
            response.setReason("Drools checks passed");
            kieSession.setGlobal("fraudResponse", response);
            kieSession.insert(request);
            kieSession.fireAllRules();
            if (response.isFraud()) {
                return response;
            }
            return new FraudCheckResponse(false, "Amount check passed");
        } finally {
            kieSession.dispose();
        }
    }
} 