package com.fintech.cms.controller.impl;

import com.fintech.cms.controller.ICardController;
import com.fintech.cms.dto.card.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.service.impl.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController implements ICardController {
    
    private final CardService cardService;
    
    @PostMapping
    public ResponseEntity<SuccessResponse> createCard(@Valid @RequestBody CreateCardRequest cardRequest) {
        SuccessResponse response = cardService.createCard(cardRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{cardId}/activate")
    public ResponseEntity<SuccessResponse> activateCard(@PathVariable String cardId) {
        SuccessResponse response = cardService.activateCard(cardId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping("/{cardId}/deactivate")
    public ResponseEntity<SuccessResponse> deactivateCard(@PathVariable String cardId) {
        SuccessResponse response = cardService.deactivateCard(cardId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{cardId}")
    public ResponseEntity<CardDetailsResponse> getCardById(@PathVariable String cardId) {
        CardDetailsResponse response = cardService.getCardById(cardId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<Page<CardListResponse>> getAllCards(Pageable pageable) {
        Page<CardListResponse> response = cardService.getAllCards(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}