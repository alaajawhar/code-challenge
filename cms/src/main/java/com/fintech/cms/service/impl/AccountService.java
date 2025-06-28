package com.fintech.cms.service.impl;

import com.fintech.cms.dto.account.*;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.entity.Account;
import com.fintech.cms.enums.AccountStatusEnum;
import com.fintech.cms.enums.TransactionTypeEnum;
import com.fintech.cms.exception.AccountException;
import com.fintech.cms.mapper.AccountMapper;
import com.fintech.cms.repository.AccountRepository;
import com.fintech.cms.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public SuccessResponse createAccount(CreateAccountRequest accountRequest) {
        Account account = accountMapper.toEntity(accountRequest);
        Account savedAccount = accountRepository.save(account);
        return new SuccessResponse("Account created successfully");
    }

    public AccountDetailsResponse getAccountById(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("Account not found with id: " + accountId));
        return accountMapper.toDetailsResponse(account);
    }

    public Page<AccountListResponse> getAllAccounts(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts.map(accountMapper::toListResponse);
    }

    public SuccessResponse updateAccount(UpdateAccountRequest accountRequest) {
        Account account = accountRepository.findById(accountRequest.getId())
                .orElseThrow(() -> new AccountException("Account not found with id: " + accountRequest.getId()));
        
        accountMapper.updateEntity(account, accountRequest);
        accountRepository.save(account);
        return new SuccessResponse("Account updated successfully");
    }

    public void deleteAccount(String accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountException("Account not found with id: " + accountId);
        }

        accountRepository.deleteById(accountId);
    }

    public boolean isAccountEligible(Account account, TransactionTypeEnum transactionType, BigDecimal amount) {
        if (!AccountStatusEnum.ACTIVE.equals(account.getStatus())) {
            return false;
        }

        if (TransactionTypeEnum.D.equals(transactionType)) {
            return account.getBalance().compareTo(amount) >= 0;
        }

        return true;
    }

    public Account updateBalance(Account account, TransactionTypeEnum transactionType, BigDecimal amount) {
        BigDecimal newBalance;
        if (TransactionTypeEnum.D.equals(transactionType)) {
            newBalance = account.getBalance().subtract(amount);
        } else {
            newBalance = account.getBalance().add(amount);
        }
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
    
    public Account getAccountEntityById(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("Account not found with id: " + accountId));
    }
}