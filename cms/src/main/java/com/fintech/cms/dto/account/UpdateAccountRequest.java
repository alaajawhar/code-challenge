package com.fintech.cms.dto.account;

import com.fintech.cms.enums.AccountStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {
    
    @NotBlank(message = "Account ID is required")
    private String id;
    
    @NotBlank(message = "Account name is required")
    private String name;
    
    @NotNull(message = "Status is required")
    private AccountStatusEnum status;
    
    @NotNull(message = "Balance is required")
    private BigDecimal balance;
    
}