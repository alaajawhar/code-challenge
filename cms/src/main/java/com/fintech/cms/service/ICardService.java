package com.fintech.cms.service;

import com.fintech.cms.dto.card.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICardService {
    
    SuccessResponse createCard(CreateCardRequest cardRequest);
    
    SuccessResponse activateCard(String cardId);
    
    SuccessResponse deactivateCard(String cardId);
    
    CardDetailsResponse getCardById(String cardId);
    
    Page<CardListResponse> getAllCards(Pageable pageable);
    
    boolean isCardEligible(Card card);
    
    Card getCardEntityById(String cardId);
}