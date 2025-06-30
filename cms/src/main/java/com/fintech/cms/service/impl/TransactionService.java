package com.fintech.cms.service.impl;

import com.fintech.cms.dto.transaction.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.entity.Account;
import com.fintech.cms.entity.Card;
import com.fintech.cms.entity.Transaction;
import com.fintech.cms.exception.TransactionException;
import com.fintech.cms.mapper.TransactionMapper;
import com.fintech.cms.repository.TransactionRepository;
import com.fintech.cms.service.ITransactionService;
import com.fintech.cms.client.FraudDetectionClient;
import com.fintech.cms.dto.fraud.FraudCheckRequest;
import com.fintech.cms.dto.fraud.FraudCheckResponse;
import lombok.RequiredArgsConstructor;
import com.fintech.cms.dto.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    
    private final TransactionRepository transactionRepository;
    private final CardService cardService;
    private final AccountService accountService;
    private final TransactionMapper transactionMapper;
    private final FraudDetectionClient fraudDetectionClient;
    
    @Transactional
    public SuccessResponse createTransaction(CreateTransactionRequest transactionRequest) {
        Account account = accountService.getAccountEntityById(transactionRequest.getAccountId());
        Card card = cardService.getCardEntityById(transactionRequest.getCardId());
        
        if (!cardService.isCardEligible(card)) {
            saveTransaction(transactionRequest, account, card, "Card is not eligible for transaction");
            throw new TransactionException("Card is not eligible for transaction");
        }
        
        if (!accountService.isAccountEligible(account, transactionRequest.getTransactionType(), 
                                            transactionRequest.getTransactionAmount())) {
            saveTransaction(transactionRequest, account, card, "Account is not eligible for transaction");
            throw new TransactionException("Account is not eligible for transaction");
        }
        
        FraudCheckRequest fraudRequest = new FraudCheckRequest(card.getCardNumber(), transactionRequest.getTransactionAmount());
        FraudCheckResponse fraudResponse = fraudDetectionClient.checkForFraud(fraudRequest).getBody();
        
        if (fraudResponse != null && fraudResponse.isFraud()) {
            String reason = "Transaction blocked due to fraud: " + fraudResponse.getReason();
            saveTransaction(transactionRequest, account, card, reason);
            throw new TransactionException(reason);
        }
        
        accountService.updateBalance(account, transactionRequest.getTransactionType(), 
                                   transactionRequest.getTransactionAmount());

        saveTransaction(transactionRequest, account, card, fraudResponse != null ? fraudResponse.getReason() : "Transaction approved");
        return new SuccessResponse("Transaction processed successfully");
    }
    
    private void saveTransaction(CreateTransactionRequest request, Account account, Card card, String response) {
        Transaction transaction = transactionMapper.toEntity(request, account, card, response);
        transaction.setTransactionDate(new Date());
        transactionRepository.save(transaction);
    }
    
    public TransactionDetailsResponse getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionException("Transaction not found with id: " + transactionId));
        return transactionMapper.toDetailsResponse(transaction);
    }
    
    public PaginatedResponse<TransactionListResponse> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return new PaginatedResponse<>(transactions.map(transactionMapper::toResponseItem).getContent(), transactions.getTotalElements());
    }
}