package com.fraud.service;

import com.fraud.dto.FraudCheckResponse;
import com.fraud.dto.FraudCheckRequest;

public interface IFraudDetectionService {
    FraudCheckResponse checkForFraud(FraudCheckRequest request);
} 