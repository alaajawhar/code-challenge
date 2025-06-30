package com.fintech.cms.controller;

import com.fintech.cms.dto.transaction.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.dto.PaginatedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ITransactionController {
    
    ResponseEntity<SuccessResponse> createTransaction(CreateTransactionRequest transactionRequest);
    
    ResponseEntity<TransactionDetailsResponse> getTransactionById(String transactionId);
    
    ResponseEntity<PaginatedResponse<TransactionListResponse>> getAllTransactions(Pageable pageable);
}