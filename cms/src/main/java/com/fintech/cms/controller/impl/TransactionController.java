package com.fintech.cms.controller.impl;

import com.fintech.cms.controller.ITransactionController;
import com.fintech.cms.dto.transaction.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.service.impl.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fintech.cms.dto.PaginatedResponse;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController implements ITransactionController {
    
    private final TransactionService transactionService;
    
    @PostMapping
    public ResponseEntity<SuccessResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest transactionRequest) {
        SuccessResponse response = transactionService.createTransaction(transactionRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDetailsResponse> getTransactionById(@PathVariable String transactionId) {
        TransactionDetailsResponse response = transactionService.getTransactionById(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<PaginatedResponse<TransactionListResponse>> getAllTransactions(Pageable pageable) {
        PaginatedResponse<TransactionListResponse> response = transactionService.getAllTransactions(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}