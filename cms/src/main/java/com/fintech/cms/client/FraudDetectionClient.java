package com.fintech.cms.client;

import com.fintech.cms.dto.fraud.FraudCheckRequest;
import com.fintech.cms.dto.fraud.FraudCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fraud-detection-service", url = "${fraud.service.url}")
public interface FraudDetectionClient {
    
    @PostMapping("/fds/api/fraud/check")
    ResponseEntity<FraudCheckResponse> checkForFraud(@RequestBody FraudCheckRequest request);
}