package com.fintech.cms.mapper;

import com.fintech.cms.dto.card.*;
import com.fintech.cms.entity.Account;
import com.fintech.cms.entity.Card;
import com.fintech.cms.enums.CardStatusEnum;
import com.fintech.cms.service.impl.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapper {
    
    private final EncryptionService encryptionService;
    
    public Card toEntity(CreateCardRequest dto, Account account) {
        Card card = new Card();
        card.setStatus(CardStatusEnum.INACTIVE);
        card.setExpiry(dto.getExpiry());
        card.setCardNumber(dto.getCardNumber());
        card.setAccount(account);
        return card;
    }
    
    public CreateCardResponse toCreateResponse(Card entity) {
        CreateCardResponse dto = new CreateCardResponse();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setExpiry(entity.getExpiry());
        dto.setAccountId(entity.getAccount().getId());
        return dto;
    }
    
    public CardDetailsResponse toDetailsResponse(Card entity) {
        CardDetailsResponse dto = new CardDetailsResponse();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setExpiry(entity.getExpiry());
        dto.setMaskedCardNumber(maskCardNumber(entity.getCardNumber()));
        dto.setAccountId(entity.getAccount().getId());
        return dto;
    }
    
    public CardStatusResponse toStatusResponse(Card entity) {
        CardStatusResponse dto = new CardStatusResponse();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        return dto;
    }
    
    public CardSummaryResponse toSummaryResponse(Card entity) {
        CardSummaryResponse dto = new CardSummaryResponse();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setExpiry(entity.getExpiry());
        dto.setMaskedCardNumber(maskCardNumber(entity.getCardNumber()));
        return dto;
    }
    
    public CardListResponse toListResponse(Card entity) {
        CardListResponse dto = new CardListResponse();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setExpiry(entity.getExpiry());
        dto.setMaskedCardNumber(maskCardNumber(entity.getCardNumber()));
        dto.setAccountId(entity.getAccount().getId());
        return dto;
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        String decrypted = encryptionService.decrypt(cardNumber);
        return "****-****-****-" + decrypted.substring(decrypted.length() - 4);
    }
}