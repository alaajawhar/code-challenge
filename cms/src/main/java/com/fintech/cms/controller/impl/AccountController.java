package com.fintech.cms.controller.impl;

import com.fintech.cms.controller.IAccountController;
import com.fintech.cms.dto.account.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.service.impl.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fintech.cms.dto.PaginatedResponse;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController implements IAccountController {
    
    private final AccountService accountService;
    
    @PostMapping
    public ResponseEntity<SuccessResponse> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest) {
        SuccessResponse response = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDetailsResponse> getAccountById(@PathVariable String accountId) {
        AccountDetailsResponse response = accountService.getAccountById(accountId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<PaginatedResponse<AccountListResponse>> getAllAccounts(Pageable pageable) {
        PaginatedResponse<AccountListResponse> response = accountService.getAllAccounts(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping
    public ResponseEntity<SuccessResponse> updateAccount(@Valid @RequestBody UpdateAccountRequest accountRequest) {
        SuccessResponse response = accountService.updateAccount(accountRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}