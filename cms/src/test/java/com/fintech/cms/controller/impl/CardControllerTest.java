package com.fintech.cms.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.cms.dto.PaginatedResponse;
import com.fintech.cms.dto.card.CardDetailsResponse;
import com.fintech.cms.dto.card.CardListResponse;
import com.fintech.cms.dto.card.CreateCardRequest;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.exception.CardException;
import com.fintech.cms.service.impl.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateCardRequest createCardRequest;
    private CardDetailsResponse cardDetailsResponse;
    private CardListResponse cardListResponse;
    private SuccessResponse successResponse;
    private PaginatedResponse<CardListResponse> paginatedResponse;

    @BeforeEach
    void setUp() {
        createCardRequest = new CreateCardRequest();
        createCardRequest.setAccountId("acc-123");
        createCardRequest.setCardNumber("1234567812345678");

        cardDetailsResponse = new CardDetailsResponse();
        cardDetailsResponse.setId("card-123");
        cardDetailsResponse.setCardNumber("1234567812345678");

        cardListResponse = new CardListResponse();
        cardListResponse.setId("card-123");
        cardListResponse.setCardNumber("1234567812345678");

        successResponse = new SuccessResponse("Card created successfully");

        paginatedResponse = new PaginatedResponse<>(Arrays.asList(cardListResponse), 1L);
    }

    @Test
    void createCard_ValidationError() throws Exception {
        createCardRequest.setAccountId(null);

        mockMvc.perform(post("/api/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCardRequest)))
                .andExpect(status().isBadRequest());

        verify(cardService, never()).createCard(any(CreateCardRequest.class));
    }

    @Test
    void getCardById_Success() throws Exception {
        when(cardService.getCardById("card-123")).thenReturn(cardDetailsResponse);

        mockMvc.perform(get("/api/cards/{id}", "card-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("card-123"))
                .andExpect(jsonPath("$.cardNumber").value("1234567812345678"));

        verify(cardService, times(1)).getCardById("card-123");
    }

    @Test
    void getCardById_NotFound() throws Exception {
        when(cardService.getCardById("card-123")).thenThrow(new CardException("Card not found"));

        mockMvc.perform(get("/api/cards/{id}", "card-123"))
                .andExpect(status().isBadRequest());

        verify(cardService, times(1)).getCardById("card-123");
    }

    @Test
    void getAllCards_Success() throws Exception {
        when(cardService.getAllCards(any(Pageable.class))).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/cards")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].id").value("card-123"))
                .andExpect(jsonPath("$.totalCount").value(1));

        verify(cardService, times(1)).getAllCards(any(Pageable.class));
    }

    @Test
    void activateCard_Success() throws Exception {
        when(cardService.activateCard("card-123")).thenReturn(successResponse);

        mockMvc.perform(put("/api/cards/{id}/activate", "card-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Card created successfully"));

        verify(cardService, times(1)).activateCard("card-123");
    }

    @Test
    void activateCard_NotFound() throws Exception {
        when(cardService.activateCard("card-123")).thenThrow(new CardException("Card not found"));

        mockMvc.perform(put("/api/cards/{id}/activate", "card-123"))
                .andExpect(status().isBadRequest());

        verify(cardService, times(1)).activateCard("card-123");
    }

    @Test
    void deactivateCard_Success() throws Exception {
        when(cardService.deactivateCard("card-123")).thenReturn(successResponse);

        mockMvc.perform(put("/api/cards/{id}/deactivate", "card-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Card created successfully"));

        verify(cardService, times(1)).deactivateCard("card-123");
    }

    @Test
    void deactivateCard_NotFound() throws Exception {
        when(cardService.deactivateCard("card-123")).thenThrow(new CardException("Card not found"));

        mockMvc.perform(put("/api/cards/{id}/deactivate", "card-123"))
                .andExpect(status().isBadRequest());

        verify(cardService, times(1)).deactivateCard("card-123");
    }
}