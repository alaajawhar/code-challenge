package com.fintech.cms.dto;

import com.fintech.cms.enums.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    
    private String id;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    private TransactionTypeEnum transactionType;
    private String accountId;
    private String cardId;
    
}