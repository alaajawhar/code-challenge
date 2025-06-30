package com.fintech.cms.service.impl;

import com.fintech.cms.dto.card.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.entity.Account;
import com.fintech.cms.entity.Card;
import com.fintech.cms.enums.CardStatusEnum;
import com.fintech.cms.exception.CardException;
import com.fintech.cms.mapper.CardMapper;
import com.fintech.cms.repository.AccountRepository;
import com.fintech.cms.repository.CardRepository;
import com.fintech.cms.service.ICardService;
import lombok.RequiredArgsConstructor;
import com.fintech.cms.dto.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CardService implements ICardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final EncryptionService encryptionService;
    private final CardMapper cardMapper;

    public SuccessResponse createCard(CreateCardRequest cardRequest) {
        Account account = accountRepository.findById(cardRequest.getAccountId())
                .orElseThrow(() -> new CardException("Account not found with id: " + cardRequest.getAccountId()));

        Card card = cardMapper.toEntity(cardRequest, account);
        card.setCardNumber(encryptionService.encrypt(card.getCardNumber()));
        Card savedCard = cardRepository.save(card);
        return new SuccessResponse("Card created successfully");
    }

    public SuccessResponse activateCard(String cardId) {
        this.updateCardStatus(cardId, CardStatusEnum.ACTIVE);
        return new SuccessResponse("Card activated successfully");
    }

    public SuccessResponse deactivateCard(String cardId) {
        this.updateCardStatus(cardId, CardStatusEnum.INACTIVE);
        return new SuccessResponse("Card deactivated successfully");
    }

    private void updateCardStatus(String cardId, CardStatusEnum cardStatusEnum) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardException("Card not found with id: " + cardId));

        card.setStatus(cardStatusEnum);
        cardRepository.save(card);
    }

    public CardDetailsResponse getCardById(String cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardException("Card not found with id: " + cardId));
        return cardMapper.toDetailsResponse(card);
    }

    public PaginatedResponse<CardListResponse> getAllCards(Pageable pageable) {
        Page<Card> cards = cardRepository.findAll(pageable);
        return new PaginatedResponse<>(cards.map(cardMapper::toResponseItem).getContent(), cards.getTotalElements());
    }

    public boolean isCardEligible(Card card) {
        return card.getStatus() == CardStatusEnum.ACTIVE &&
                card.getExpiry().after(new Date());
    }

    public Card getCardEntityById(String cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardException("Card not found with id: " + cardId));
    }
}