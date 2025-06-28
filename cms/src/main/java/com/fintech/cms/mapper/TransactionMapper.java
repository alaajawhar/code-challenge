package com.fintech.cms.mapper;

import com.fintech.cms.dto.transaction.*;
import com.fintech.cms.entity.Account;
import com.fintech.cms.entity.Card;
import com.fintech.cms.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    
    public Transaction toEntity(CreateTransactionRequest dto, Account account, Card card) {
        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(dto.getTransactionAmount());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setAccount(account);
        transaction.setCard(card);
        return transaction;
    }
    
    public CreateTransactionResponse toCreateResponse(Transaction entity) {
        CreateTransactionResponse dto = new CreateTransactionResponse();
        dto.setId(entity.getId());
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setTransactionType(entity.getTransactionType());
        return dto;
    }
    
    public TransactionDetailsResponse toDetailsResponse(Transaction entity) {
        TransactionDetailsResponse dto = new TransactionDetailsResponse();
        dto.setId(entity.getId());
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setTransactionType(entity.getTransactionType());
        dto.setAccountId(entity.getAccount().getId());
        dto.setCardId(entity.getCard().getId());
        return dto;
    }
    
    public TransactionSummaryResponse toSummaryResponse(Transaction entity) {
        TransactionSummaryResponse dto = new TransactionSummaryResponse();
        dto.setId(entity.getId());
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setTransactionType(entity.getTransactionType());
        return dto;
    }
    
    public TransactionListResponse toListResponse(Transaction entity) {
        TransactionListResponse dto = new TransactionListResponse();
        dto.setId(entity.getId());
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setTransactionType(entity.getTransactionType());
        dto.setAccountId(entity.getAccount().getId());
        dto.setCardId(entity.getCard().getId());
        return dto;
    }
}