package com.fintech.cms.mapper;

import com.fintech.cms.dto.account.*;
import com.fintech.cms.entity.Account;
import com.fintech.cms.enums.AccountStatusEnum;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    
    public Account toEntity(CreateAccountRequest dto) {
        Account account = new Account();
        account.setName(dto.getName());
        account.setStatus(AccountStatusEnum.ACTIVE);
        account.setBalance(dto.getBalance());
        return account;
    }
    
    public CreateAccountResponse toCreateResponse(Account entity) {
        CreateAccountResponse dto = new CreateAccountResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setBalance(entity.getBalance());
        return dto;
    }
    
    public AccountDetailsResponse toDetailsResponse(Account entity) {
        AccountDetailsResponse dto = new AccountDetailsResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setBalance(entity.getBalance());
        return dto;
    }
    
    public AccountSummaryResponse toSummaryResponse(Account entity) {
        AccountSummaryResponse dto = new AccountSummaryResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setBalance(entity.getBalance());
        return dto;
    }
    
    public AccountListResponse toListResponse(Account entity) {
        AccountListResponse dto = new AccountListResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setBalance(entity.getBalance());
        return dto;
    }
    
    public void updateEntity(Account account, UpdateAccountRequest dto) {
        account.setName(dto.getName());
        account.setStatus(dto.getStatus());
        account.setBalance(dto.getBalance());
    }
}