package com.fintech.cms.controller;

import com.fintech.cms.dto.transaction.*;
import com.fintech.cms.dto.common.SuccessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ITransactionController {
    
    ResponseEntity<SuccessResponse> createTransaction(CreateTransactionRequest transactionRequest);
    
    ResponseEntity<TransactionDetailsResponse> getTransactionById(String transactionId);
    
    ResponseEntity<Page<TransactionListResponse>> getAllTransactions(Pageable pageable);
}