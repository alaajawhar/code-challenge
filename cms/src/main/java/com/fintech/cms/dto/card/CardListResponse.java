package com.fintech.cms.dto.card;

import com.fintech.cms.enums.CardStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardListResponse {
    
    private String id;
    private CardStatusEnum status;
    private Date expiry;
    private String maskedCardNumber;
    private String cardNumber;
    private String accountId;
    
}