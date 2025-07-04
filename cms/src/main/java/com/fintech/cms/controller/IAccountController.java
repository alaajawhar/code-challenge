package com.fintech.cms.controller;

import com.fintech.cms.dto.account.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.dto.PaginatedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IAccountController {
    
    ResponseEntity<SuccessResponse> createAccount(CreateAccountRequest accountRequest);
    
    ResponseEntity<AccountDetailsResponse> getAccountById(String accountId);
    
    ResponseEntity<PaginatedResponse<AccountListResponse>> getAllAccounts(Pageable pageable);
    
    ResponseEntity<SuccessResponse> updateAccount(UpdateAccountRequest accountRequest);
    
    ResponseEntity<Void> deleteAccount(String accountId);
}