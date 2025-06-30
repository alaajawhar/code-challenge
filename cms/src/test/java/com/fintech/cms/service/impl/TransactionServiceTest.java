package com.fintech.cms.service.impl;

import com.fintech.cms.client.FraudDetectionClient;
import com.fintech.cms.dto.PaginatedResponse;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.dto.fraud.FraudCheckRequest;
import com.fintech.cms.dto.fraud.FraudCheckResponse;
import com.fintech.cms.dto.transaction.CreateTransactionRequest;
import com.fintech.cms.dto.transaction.TransactionDetailsResponse;
import com.fintech.cms.dto.transaction.TransactionListResponse;
import com.fintech.cms.entity.Account;
import com.fintech.cms.entity.Card;
import com.fintech.cms.entity.Transaction;
import com.fintech.cms.enums.AccountStatusEnum;
import com.fintech.cms.enums.CardStatusEnum;
import com.fintech.cms.enums.TransactionTypeEnum;
import com.fintech.cms.exception.TransactionException;
import com.fintech.cms.mapper.TransactionMapper;
import com.fintech.cms.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CardService cardService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private FraudDetectionClient fraudDetectionClient;

    @InjectMocks
    private TransactionService transactionService;

    private Account testAccount;
    private Card testCard;
    private Transaction testTransaction;
    private CreateTransactionRequest createTransactionRequest;
    private FraudCheckResponse fraudCheckResponse;
    private TransactionDetailsResponse transactionDetailsResponse;
    private TransactionListResponse transactionListResponse;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId("acc-123");
        testAccount.setName("John Doe");
        testAccount.setBalance(new BigDecimal("1000.00"));
        testAccount.setStatus(AccountStatusEnum.ACTIVE);

        testCard = new Card();
        testCard.setId("card-123");
        testCard.setCardNumber("1234567812345678");
        testCard.setStatus(CardStatusEnum.ACTIVE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        testCard.setExpiry(calendar.getTime());

        testTransaction = new Transaction();
        testTransaction.setId("txn-123");
        testTransaction.setTransactionAmount(new BigDecimal("500.00"));
        testTransaction.setTransactionType(TransactionTypeEnum.D);
        testTransaction.setTransactionDate(new Date());
        testTransaction.setAccount(testAccount);
        testTransaction.setCard(testCard);

        createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAccountId("acc-123");
        createTransactionRequest.setCardId("card-123");
        createTransactionRequest.setTransactionAmount(new BigDecimal("500.00"));
        createTransactionRequest.setTransactionType(TransactionTypeEnum.D);

        fraudCheckResponse = new FraudCheckResponse();
        fraudCheckResponse.setFraud(false);
        fraudCheckResponse.setReason("Transaction approved");

        transactionDetailsResponse = new TransactionDetailsResponse();
        transactionDetailsResponse.setId("txn-123");
        transactionDetailsResponse.setTransactionAmount(new BigDecimal("500.00"));

        transactionListResponse = new TransactionListResponse();
        transactionListResponse.setId("txn-123");
        transactionListResponse.setTransactionAmount(new BigDecimal("500.00"));
    }

    @Test
    void createTransaction_Success() {
        when(accountService.getAccountEntityById("acc-123")).thenReturn(testAccount);
        when(cardService.getCardEntityById("card-123")).thenReturn(testCard);
        when(cardService.isCardEligible(testCard)).thenReturn(true);
        when(accountService.isAccountEligible(testAccount, TransactionTypeEnum.D, new BigDecimal("500.00"))).thenReturn(true);
        when(fraudDetectionClient.checkForFraud(any(FraudCheckRequest.class))).thenReturn(ResponseEntity.ok(fraudCheckResponse));
        when(transactionMapper.toEntity(any(), any(), any(), anyString())).thenReturn(testTransaction);
        when(transactionRepository.save(testTransaction)).thenReturn(testTransaction);

        SuccessResponse response = transactionService.createTransaction(createTransactionRequest);

        assertNotNull(response);
        assertEquals("Transaction processed successfully", response.getMessage());
        verify(accountService, times(1)).updateBalance(testAccount, TransactionTypeEnum.D, new BigDecimal("500.00"));
        verify(transactionRepository, times(1)).save(testTransaction);
    }

    @Test
    void createTransaction_CardNotEligible() {
        when(accountService.getAccountEntityById("acc-123")).thenReturn(testAccount);
        when(cardService.getCardEntityById("card-123")).thenReturn(testCard);
        when(cardService.isCardEligible(testCard)).thenReturn(false);
        when(transactionMapper.toEntity(any(), any(), any(), anyString())).thenReturn(testTransaction);
        when(transactionRepository.save(testTransaction)).thenReturn(testTransaction);

        TransactionException exception = assertThrows(TransactionException.class, 
            () -> transactionService.createTransaction(createTransactionRequest));

        assertEquals("Card is not eligible for transaction", exception.getMessage());
        verify(transactionRepository, times(1)).save(testTransaction);
        verify(accountService, never()).updateBalance(any(), any(), any());
    }

    @Test
    void createTransaction_AccountNotEligible() {
        when(accountService.getAccountEntityById("acc-123")).thenReturn(testAccount);
        when(cardService.getCardEntityById("card-123")).thenReturn(testCard);
        when(cardService.isCardEligible(testCard)).thenReturn(true);
        when(accountService.isAccountEligible(testAccount, TransactionTypeEnum.D, new BigDecimal("500.00"))).thenReturn(false);
        when(transactionMapper.toEntity(any(), any(), any(), anyString())).thenReturn(testTransaction);
        when(transactionRepository.save(testTransaction)).thenReturn(testTransaction);

        TransactionException exception = assertThrows(TransactionException.class, 
            () -> transactionService.createTransaction(createTransactionRequest));

        assertEquals("Account is not eligible for transaction", exception.getMessage());
        verify(transactionRepository, times(1)).save(testTransaction);
        verify(accountService, never()).updateBalance(any(), any(), any());
    }

    @Test
    void createTransaction_FraudDetected() {
        FraudCheckResponse fraudResponse = new FraudCheckResponse();
        fraudResponse.setFraud(true);
        fraudResponse.setReason("Suspicious activity detected");

        when(accountService.getAccountEntityById("acc-123")).thenReturn(testAccount);
        when(cardService.getCardEntityById("card-123")).thenReturn(testCard);
        when(cardService.isCardEligible(testCard)).thenReturn(true);
        when(accountService.isAccountEligible(testAccount, TransactionTypeEnum.D, new BigDecimal("500.00"))).thenReturn(true);
        when(fraudDetectionClient.checkForFraud(any(FraudCheckRequest.class))).thenReturn(ResponseEntity.ok(fraudResponse));
        when(transactionMapper.toEntity(any(), any(), any(), anyString())).thenReturn(testTransaction);
        when(transactionRepository.save(testTransaction)).thenReturn(testTransaction);

        TransactionException exception = assertThrows(TransactionException.class, 
            () -> transactionService.createTransaction(createTransactionRequest));

        assertEquals("Transaction blocked due to fraud: Suspicious activity detected", exception.getMessage());
        verify(transactionRepository, times(1)).save(testTransaction);
        verify(accountService, never()).updateBalance(any(), any(), any());
    }

    @Test
    void createTransaction_FraudCheckNullResponse() {
        when(accountService.getAccountEntityById("acc-123")).thenReturn(testAccount);
        when(cardService.getCardEntityById("card-123")).thenReturn(testCard);
        when(cardService.isCardEligible(testCard)).thenReturn(true);
        when(accountService.isAccountEligible(testAccount, TransactionTypeEnum.D, new BigDecimal("500.00"))).thenReturn(true);
        when(fraudDetectionClient.checkForFraud(any(FraudCheckRequest.class))).thenReturn(ResponseEntity.ok(null));
        when(transactionMapper.toEntity(any(), any(), any(), any())).thenReturn(testTransaction);
        when(transactionRepository.save(testTransaction)).thenReturn(testTransaction);

        SuccessResponse response = transactionService.createTransaction(createTransactionRequest);

        assertNotNull(response);
        assertEquals("Transaction processed successfully", response.getMessage());
        verify(accountService, times(1)).updateBalance(testAccount, TransactionTypeEnum.D, new BigDecimal("500.00"));
        verify(transactionRepository, times(1)).save(testTransaction);
    }

    @Test
    void getTransactionById_Success() {
        when(transactionRepository.findById("txn-123")).thenReturn(Optional.of(testTransaction));
        when(transactionMapper.toDetailsResponse(testTransaction)).thenReturn(transactionDetailsResponse);

        TransactionDetailsResponse response = transactionService.getTransactionById("txn-123");

        assertNotNull(response);
        assertEquals("txn-123", response.getId());
        verify(transactionRepository, times(1)).findById("txn-123");
    }

    @Test
    void getTransactionById_NotFound() {
        when(transactionRepository.findById("txn-123")).thenReturn(Optional.empty());

        TransactionException exception = assertThrows(TransactionException.class,
            () -> transactionService.getTransactionById("txn-123"));

        assertEquals("Transaction not found with id: txn-123", exception.getMessage());
    }

    @Test
    void getAllTransactions_Success() {
        Page<Transaction> transactionPage = new PageImpl<>(Arrays.asList(testTransaction));
        when(transactionRepository.findAll(any(Pageable.class))).thenReturn(transactionPage);
        when(transactionMapper.toResponseItem(testTransaction)).thenReturn(transactionListResponse);

        PaginatedResponse<TransactionListResponse> response = transactionService.getAllTransactions(Pageable.unpaged());

        assertNotNull(response);
        assertEquals(1, response.getTotalCount());
        assertEquals(1, response.getList().size());
    }

}