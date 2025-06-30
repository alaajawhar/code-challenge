package com.fintech.cms.service.impl;

import com.fintech.cms.dto.card.CardDetailsResponse;
import com.fintech.cms.dto.card.CardListResponse;
import com.fintech.cms.dto.card.CreateCardRequest;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.dto.PaginatedResponse;
import com.fintech.cms.entity.Account;
import com.fintech.cms.entity.Card;
import com.fintech.cms.enums.CardStatusEnum;
import com.fintech.cms.exception.CardException;
import com.fintech.cms.mapper.CardMapper;
import com.fintech.cms.repository.AccountRepository;
import com.fintech.cms.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    private Card testCard;
    private Account testAccount;
    private CreateCardRequest createCardRequest;
    private CardDetailsResponse cardDetailsResponse;
    private CardListResponse cardListResponse;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId("acc-123");
        testAccount.setName("John Doe");

        testCard = new Card();
        testCard.setId("card-123");
        testCard.setCardNumber("1234567812345678");
        testCard.setStatus(CardStatusEnum.ACTIVE);
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        testCard.setExpiry(calendar.getTime());
        testCard.setAccount(testAccount);

        createCardRequest = new CreateCardRequest();
        createCardRequest.setAccountId("acc-123");
        createCardRequest.setCardNumber("1234567812345678");

        cardDetailsResponse = new CardDetailsResponse();
        cardDetailsResponse.setId("card-123");
        cardDetailsResponse.setCardNumber("1234567812345678");

        cardListResponse = new CardListResponse();
        cardListResponse.setId("card-123");
        cardListResponse.setCardNumber("1234567812345678");
    }

    @Test
    void createCard_Success() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.of(testAccount));
        when(cardMapper.toEntity(createCardRequest, testAccount)).thenReturn(testCard);
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted-card-number");
        when(cardRepository.save(testCard)).thenReturn(testCard);

        SuccessResponse response = cardService.createCard(createCardRequest);

        assertNotNull(response);
        assertEquals("Card created successfully", response.getMessage());
        verify(encryptionService, times(1)).encrypt("1234567812345678");
        verify(cardRepository, times(1)).save(testCard);
    }

    @Test
    void createCard_AccountNotFound() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.empty());

        CardException exception = assertThrows(CardException.class, 
            () -> cardService.createCard(createCardRequest));

        assertEquals("Account not found with id: acc-123", exception.getMessage());
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void activateCard_Success() {
        when(cardRepository.findById("card-123")).thenReturn(Optional.of(testCard));
        when(cardRepository.save(testCard)).thenReturn(testCard);

        SuccessResponse response = cardService.activateCard("card-123");

        assertNotNull(response);
        assertEquals("Card activated successfully", response.getMessage());
        assertEquals(CardStatusEnum.ACTIVE, testCard.getStatus());
        verify(cardRepository, times(1)).save(testCard);
    }

    @Test
    void activateCard_CardNotFound() {
        when(cardRepository.findById("card-123")).thenReturn(Optional.empty());

        CardException exception = assertThrows(CardException.class, 
            () -> cardService.activateCard("card-123"));

        assertEquals("Card not found with id: card-123", exception.getMessage());
    }

    @Test
    void deactivateCard_Success() {
        when(cardRepository.findById("card-123")).thenReturn(Optional.of(testCard));
        when(cardRepository.save(testCard)).thenReturn(testCard);

        SuccessResponse response = cardService.deactivateCard("card-123");

        assertNotNull(response);
        assertEquals("Card deactivated successfully", response.getMessage());
        assertEquals(CardStatusEnum.INACTIVE, testCard.getStatus());
        verify(cardRepository, times(1)).save(testCard);
    }

    @Test
    void deactivateCard_CardNotFound() {
        when(cardRepository.findById("card-123")).thenReturn(Optional.empty());

        CardException exception = assertThrows(CardException.class, 
            () -> cardService.deactivateCard("card-123"));

        assertEquals("Card not found with id: card-123", exception.getMessage());
    }

    @Test
    void getCardById_Success() {
        when(cardRepository.findById("card-123")).thenReturn(Optional.of(testCard));
        when(cardMapper.toDetailsResponse(testCard)).thenReturn(cardDetailsResponse);

        CardDetailsResponse response = cardService.getCardById("card-123");

        assertNotNull(response);
        assertEquals("card-123", response.getId());
        verify(cardRepository, times(1)).findById("card-123");
    }

    @Test
    void getCardById_NotFound() {
        when(cardRepository.findById("card-123")).thenReturn(Optional.empty());

        CardException exception = assertThrows(CardException.class, 
            () -> cardService.getCardById("card-123"));

        assertEquals("Card not found with id: card-123", exception.getMessage());
    }

    @Test
    void getAllCards_Success() {
        Page<Card> cardPage = new PageImpl<>(Arrays.asList(testCard));
        when(cardRepository.findAll(any(Pageable.class))).thenReturn(cardPage);
        when(cardMapper.toResponseItem(testCard)).thenReturn(cardListResponse);

        PaginatedResponse<CardListResponse> response = cardService.getAllCards(Pageable.unpaged());

        assertNotNull(response);
        assertEquals(1, response.getTotalCount());
        assertEquals(1, response.getList().size());
    }

    @Test
    void isCardEligible_ActiveAndNotExpired() {
        testCard.setStatus(CardStatusEnum.ACTIVE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        testCard.setExpiry(calendar.getTime());

        boolean result = cardService.isCardEligible(testCard);

        assertTrue(result);
    }

    @Test
    void isCardEligible_InactiveCard() {
        testCard.setStatus(CardStatusEnum.INACTIVE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        testCard.setExpiry(calendar.getTime());

        boolean result = cardService.isCardEligible(testCard);

        assertFalse(result);
    }

    @Test
    void isCardEligible_ExpiredCard() {
        testCard.setStatus(CardStatusEnum.ACTIVE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        testCard.setExpiry(calendar.getTime());

        boolean result = cardService.isCardEligible(testCard);

        assertFalse(result);
    }

    @Test
    void isCardEligible_InactiveAndExpired() {
        testCard.setStatus(CardStatusEnum.INACTIVE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        testCard.setExpiry(calendar.getTime());

        boolean result = cardService.isCardEligible(testCard);

        assertFalse(result);
    }

    @Test
    void getCardEntityById_Success() {
        when(cardRepository.findById("card-123")).thenReturn(Optional.of(testCard));

        Card result = cardService.getCardEntityById("card-123");

        assertNotNull(result);
        assertEquals("card-123", result.getId());
    }

    @Test
    void getCardEntityById_NotFound() {
        when(cardRepository.findById("card-123")).thenReturn(Optional.empty());

        CardException exception = assertThrows(CardException.class, 
            () -> cardService.getCardEntityById("card-123"));

        assertEquals("Card not found with id: card-123", exception.getMessage());
    }
}