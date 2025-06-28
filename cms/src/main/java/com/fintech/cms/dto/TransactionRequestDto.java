package com.fintech.cms.dto;

import com.fintech.cms.enums.TransactionTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    
    @NotNull(message = "Transaction amount is required")
    private BigDecimal transactionAmount;
    
    @NotNull(message = "Transaction type is required")
    private TransactionTypeEnum transactionType;
    
    @NotNull(message = "Account ID is required")
    private String accountId;
    
    @NotNull(message = "Card ID is required")
    private String cardId;
    
}