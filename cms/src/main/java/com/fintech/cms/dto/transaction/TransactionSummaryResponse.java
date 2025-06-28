package com.fintech.cms.dto.transaction;

import com.fintech.cms.enums.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryResponse {
    
    private String id;
    private BigDecimal transactionAmount;
    private Date transactionDate;
    private TransactionTypeEnum transactionType;
    
}