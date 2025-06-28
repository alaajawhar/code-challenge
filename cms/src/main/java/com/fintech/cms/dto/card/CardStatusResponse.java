package com.fintech.cms.dto.card;

import com.fintech.cms.enums.CardStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusResponse {
    
    private String id;
    private CardStatusEnum status;
    
}