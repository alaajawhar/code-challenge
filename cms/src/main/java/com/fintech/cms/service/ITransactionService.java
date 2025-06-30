package com.fintech.cms.service;

import com.fintech.cms.dto.transaction.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.dto.PaginatedResponse;
import org.springframework.data.domain.Pageable;

public interface ITransactionService {
    
    SuccessResponse createTransaction(CreateTransactionRequest transactionRequest);
    
    TransactionDetailsResponse getTransactionById(String transactionId);
    
    PaginatedResponse<TransactionListResponse> getAllTransactions(Pageable pageable);
}