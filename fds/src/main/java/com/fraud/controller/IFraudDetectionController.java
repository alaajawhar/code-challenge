package com.fraud.controller;

import com.fraud.dto.FraudCheckResponse;
import com.fraud.dto.FraudCheckRequest;
import org.springframework.http.ResponseEntity;

public interface IFraudDetectionController {
    ResponseEntity<FraudCheckResponse> checkForFraud(FraudCheckRequest request);
} 