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
import lombok.RequiredArgsConstructor;
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
    
    @Transactional
    public SuccessResponse createTransaction(CreateTransactionRequest transactionRequest) {
        Account account = accountService.getAccountEntityById(transactionRequest.getAccountId());
        Card card = cardService.getCardEntityById(transactionRequest.getCardId());
        
        if (!cardService.isCardEligible(card)) {
            throw new TransactionException("Card is not eligible for transaction");
        }
        
        if (!accountService.isAccountEligible(account, transactionRequest.getTransactionType(), 
                                            transactionRequest.getTransactionAmount())) {
            throw new TransactionException("Account is not eligible for transaction");
        }
        
        accountService.updateBalance(account, transactionRequest.getTransactionType(), 
                                   transactionRequest.getTransactionAmount());
        
        Transaction transaction = transactionMapper.toEntity(transactionRequest, account, card);
        transaction.setTransactionDate(new Date());
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new SuccessResponse("Transaction processed successfully");
    }
    
    public TransactionDetailsResponse getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionException("Transaction not found with id: " + transactionId));
        return transactionMapper.toDetailsResponse(transaction);
    }
    
    public Page<TransactionListResponse> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.map(transactionMapper::toListResponse);
    }
}