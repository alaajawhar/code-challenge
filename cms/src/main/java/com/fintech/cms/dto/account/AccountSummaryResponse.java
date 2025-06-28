package com.fintech.cms.dto.account;

import com.fintech.cms.enums.AccountStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryResponse {
    
    private String id;
    private String name;
    private AccountStatusEnum status;
    private BigDecimal balance;
    
}