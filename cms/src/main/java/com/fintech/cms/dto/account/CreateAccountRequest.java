package com.fintech.cms.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    
    @NotBlank(message = "Account name is required")
    private String name;
    
    @NotNull(message = "Initial balance is required")
    private BigDecimal balance;
    
}