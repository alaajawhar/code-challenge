package com.fintech.cms.dto;

import com.fintech.cms.enums.AccountStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {
    
    @NotNull(message = "Status is required")
    private AccountStatusEnum status;
    
    @NotNull(message = "Balance is required")
    private BigDecimal balance;
    
}