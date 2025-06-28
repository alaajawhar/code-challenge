package com.fintech.cms.service;

import com.fintech.cms.dto.transaction.*;
import com.fintech.cms.dto.common.SuccessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransactionService {
    
    SuccessResponse createTransaction(CreateTransactionRequest transactionRequest);
    
    TransactionDetailsResponse getTransactionById(String transactionId);
    
    Page<TransactionListResponse> getAllTransactions(Pageable pageable);
}