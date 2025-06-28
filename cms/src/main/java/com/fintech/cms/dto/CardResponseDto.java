package com.fintech.cms.dto;

import com.fintech.cms.enums.CardStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {
    
    private String id;
    private CardStatusEnum status;
    private Date expiry;
    private String cardNumber;
    private String accountId;
    
}