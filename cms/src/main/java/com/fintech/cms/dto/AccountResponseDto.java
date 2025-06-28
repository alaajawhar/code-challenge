package com.fintech.cms.dto;

import com.fintech.cms.enums.AccountStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    
    private String id;
    private AccountStatusEnum status;
    private BigDecimal balance;
    
}