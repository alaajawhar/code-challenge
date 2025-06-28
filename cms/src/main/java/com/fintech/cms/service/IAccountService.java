package com.fintech.cms.service;

import com.fintech.cms.dto.account.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.entity.Account;
import com.fintech.cms.enums.TransactionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface IAccountService {
    
    SuccessResponse createAccount(CreateAccountRequest accountRequest);
    
    AccountDetailsResponse getAccountById(String accountId);
    
    Page<AccountListResponse> getAllAccounts(Pageable pageable);
    
    SuccessResponse updateAccount(UpdateAccountRequest accountRequest);
    
    void deleteAccount(String accountId);
    
    boolean isAccountEligible(Account account, TransactionTypeEnum transactionType, BigDecimal amount);
    
    Account updateBalance(Account account, TransactionTypeEnum transactionType, BigDecimal amount);
    
    Account getAccountEntityById(String accountId);
}