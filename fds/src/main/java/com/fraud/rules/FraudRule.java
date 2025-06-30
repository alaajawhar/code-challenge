package com.fraud.rules;

import com.fraud.dto.FraudCheckResponse;
import com.fraud.dto.FraudCheckRequest;

public interface FraudRule {
    FraudCheckResponse check(FraudCheckRequest request);
} 