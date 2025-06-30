package com.fintech.cms.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.cms.dto.PaginatedResponse;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.dto.transaction.CreateTransactionRequest;
import com.fintech.cms.dto.transaction.TransactionDetailsResponse;
import com.fintech.cms.dto.transaction.TransactionListResponse;
import com.fintech.cms.enums.TransactionTypeEnum;
import com.fintech.cms.exception.TransactionException;
import com.fintech.cms.service.impl.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateTransactionRequest createTransactionRequest;
    private TransactionDetailsResponse transactionDetailsResponse;
    private TransactionListResponse transactionListResponse;
    private SuccessResponse successResponse;
    private PaginatedResponse<TransactionListResponse> paginatedResponse;

    @BeforeEach
    void setUp() {
        createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAccountId("acc-123");
        createTransactionRequest.setCardId("card-123");
        createTransactionRequest.setTransactionAmount(new BigDecimal("500.00"));
        createTransactionRequest.setTransactionType(TransactionTypeEnum.D);

        transactionDetailsResponse = new TransactionDetailsResponse();
        transactionDetailsResponse.setId("txn-123");
        transactionDetailsResponse.setTransactionAmount(new BigDecimal("500.00"));
        transactionDetailsResponse.setTransactionType(TransactionTypeEnum.D);
        transactionDetailsResponse.setTransactionDate(new Date());

        transactionListResponse = new TransactionListResponse();
        transactionListResponse.setId("txn-123");
        transactionListResponse.setTransactionAmount(new BigDecimal("500.00"));
        transactionListResponse.setTransactionType(TransactionTypeEnum.D);

        successResponse = new SuccessResponse("Transaction processed successfully");

        paginatedResponse = new PaginatedResponse<>(Arrays.asList(transactionListResponse), 1L);
    }

    @Test
    void createTransaction_Success() throws Exception {
        when(transactionService.createTransaction(any(CreateTransactionRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTransactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Transaction processed successfully"));

        verify(transactionService, times(1)).createTransaction(any(CreateTransactionRequest.class));
    }

    @Test
    void createTransaction_ValidationError() throws Exception {
        createTransactionRequest.setAccountId(null);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTransactionRequest)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).createTransaction(any(CreateTransactionRequest.class));
    }

    @Test
    void createTransaction_CardNotEligible() throws Exception {
        when(transactionService.createTransaction(any(CreateTransactionRequest.class)))
                .thenThrow(new TransactionException("Card is not eligible for transaction"));

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTransactionRequest)))
                .andExpect(status().isBadRequest());

        verify(transactionService, times(1)).createTransaction(any(CreateTransactionRequest.class));
    }

    @Test
    void createTransaction_AccountNotEligible() throws Exception {
        when(transactionService.createTransaction(any(CreateTransactionRequest.class)))
                .thenThrow(new TransactionException("Account is not eligible for transaction"));

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTransactionRequest)))
                .andExpect(status().isBadRequest());

        verify(transactionService, times(1)).createTransaction(any(CreateTransactionRequest.class));
    }

    @Test
    void createTransaction_FraudDetected() throws Exception {
        when(transactionService.createTransaction(any(CreateTransactionRequest.class)))
                .thenThrow(new TransactionException("Transaction blocked due to fraud: Suspicious activity"));

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTransactionRequest)))
                .andExpect(status().isBadRequest());

        verify(transactionService, times(1)).createTransaction(any(CreateTransactionRequest.class));
    }

    @Test
    void getTransactionById_Success() throws Exception {
        when(transactionService.getTransactionById("txn-123")).thenReturn(transactionDetailsResponse);

        mockMvc.perform(get("/api/transactions/{id}", "txn-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("txn-123"))
                .andExpect(jsonPath("$.transactionAmount").value(500.00))
                .andExpect(jsonPath("$.transactionType").value("D"));

        verify(transactionService, times(1)).getTransactionById("txn-123");
    }

    @Test
    void getTransactionById_NotFound() throws Exception {
        when(transactionService.getTransactionById("txn-123"))
                .thenThrow(new TransactionException("Transaction not found"));

        mockMvc.perform(get("/api/transactions/{id}", "txn-123"))
                .andExpect(status().isBadRequest());

        verify(transactionService, times(1)).getTransactionById("txn-123");
    }

    @Test
    void getAllTransactions_Success() throws Exception {
        when(transactionService.getAllTransactions(any(Pageable.class))).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/transactions")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].id").value("txn-123"))
                .andExpect(jsonPath("$.list[0].transactionAmount").value(500.00))
                .andExpect(jsonPath("$.totalCount").value(1));

        verify(transactionService, times(1)).getAllTransactions(any(Pageable.class));
    }

    @Test
    void getAllTransactions_EmptyResult() throws Exception {
        PaginatedResponse<TransactionListResponse> emptyResponse = new PaginatedResponse<>(Arrays.asList(), 0L);
        when(transactionService.getAllTransactions(any(Pageable.class))).thenReturn(emptyResponse);

        mockMvc.perform(get("/api/transactions")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list").isEmpty())
                .andExpect(jsonPath("$.totalCount").value(0));

        verify(transactionService, times(1)).getAllTransactions(any(Pageable.class));
    }
}