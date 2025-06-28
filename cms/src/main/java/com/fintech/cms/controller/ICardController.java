package com.fintech.cms.controller;

import com.fintech.cms.dto.card.*;
import com.fintech.cms.dto.common.SuccessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ICardController {
    
    ResponseEntity<SuccessResponse> createCard(CreateCardRequest cardRequest);
    
    ResponseEntity<SuccessResponse> activateCard(String cardId);
    
    ResponseEntity<SuccessResponse> deactivateCard(String cardId);
    
    ResponseEntity<CardDetailsResponse> getCardById(String cardId);
    
    ResponseEntity<Page<CardListResponse>> getAllCards(Pageable pageable);
}