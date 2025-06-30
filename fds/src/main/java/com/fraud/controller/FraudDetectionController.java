package com.fraud.controller;

import com.fraud.dto.FraudCheckRequest;
import com.fraud.dto.FraudCheckResponse;
import com.fraud.service.FraudDetectionService;
import com.fraud.controller.IFraudDetectionController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
@Slf4j
public class FraudDetectionController implements IFraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    @PostMapping("/check")
    @Override
    public ResponseEntity<FraudCheckResponse> checkForFraud(@Valid @RequestBody FraudCheckRequest request) {
        log.info("Received fraud check request for card: {}", request.getCardNumber());
        
        FraudCheckResponse response = fraudDetectionService.checkForFraud(request);
        
        return ResponseEntity.ok(response);
    }
} 