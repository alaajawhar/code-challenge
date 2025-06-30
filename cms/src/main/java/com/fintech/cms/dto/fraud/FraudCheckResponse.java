package com.fintech.cms.dto.fraud;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraudCheckResponse {
    
    private boolean isFraud;
    private String reason;
}