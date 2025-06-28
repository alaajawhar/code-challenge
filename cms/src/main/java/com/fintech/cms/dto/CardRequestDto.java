package com.fintech.cms.dto;

import com.fintech.cms.enums.CardStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {
    
    @NotNull(message = "Status is required")
    private CardStatusEnum status;
    
    @NotNull(message = "Expiry date is required")
    private Date expiry;
    
    @NotBlank(message = "Card number is required")
    private String cardNumber;
    
    @NotNull(message = "Account ID is required")
    private String accountId;
    
}