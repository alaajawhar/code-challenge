package com.fintech.cms.service.impl;

import com.fintech.cms.dto.account.AccountDetailsResponse;
import com.fintech.cms.dto.account.AccountListResponse;
import com.fintech.cms.dto.account.CreateAccountRequest;
import com.fintech.cms.dto.account.UpdateAccountRequest;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.dto.PaginatedResponse;
import com.fintech.cms.entity.Account;
import com.fintech.cms.enums.AccountStatusEnum;
import com.fintech.cms.enums.TransactionTypeEnum;
import com.fintech.cms.exception.AccountException;
import com.fintech.cms.mapper.AccountMapper;
import com.fintech.cms.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;
    private CreateAccountRequest createAccountRequest;
    private UpdateAccountRequest updateAccountRequest;
    private AccountDetailsResponse accountDetailsResponse;
    private AccountListResponse accountListResponse;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId("acc-123");
        testAccount.setName("John Doe");
        testAccount.setBalance(new BigDecimal("1000.00"));
        testAccount.setStatus(AccountStatusEnum.ACTIVE);

        createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setName("John Doe");
        createAccountRequest.setBalance(new BigDecimal("1000.00"));

        updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setId("acc-123");
        updateAccountRequest.setBalance(new BigDecimal("1500.00"));

        accountDetailsResponse = new AccountDetailsResponse();
        accountDetailsResponse.setId("acc-123");
        accountDetailsResponse.setName("John Doe");
        accountDetailsResponse.setBalance(new BigDecimal("1000.00"));

        accountListResponse = new AccountListResponse();
        accountListResponse.setId("acc-123");
        accountListResponse.setName("John Doe");
    }

    @Test
    void createAccount_Success() {
        when(accountMapper.toEntity(createAccountRequest)).thenReturn(testAccount);
        when(accountRepository.save(testAccount)).thenReturn(testAccount);

        SuccessResponse response = accountService.createAccount(createAccountRequest);

        assertNotNull(response);
        assertEquals("Account created successfully", response.getMessage());
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void getAccountById_Success() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.of(testAccount));
        when(accountMapper.toDetailsResponse(testAccount)).thenReturn(accountDetailsResponse);

        AccountDetailsResponse response = accountService.getAccountById("acc-123");

        assertNotNull(response);
        assertEquals("acc-123", response.getId());
        verify(accountRepository, times(1)).findById("acc-123");
    }

    @Test
    void getAccountById_NotFound() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, 
            () -> accountService.getAccountById("acc-123"));

        assertEquals("Account not found with id: acc-123", exception.getMessage());
    }

    @Test
    void getAllAccounts_Success() {
        Page<Account> accountPage = new PageImpl<>(Arrays.asList(testAccount));
        when(accountRepository.findAll(any(Pageable.class))).thenReturn(accountPage);
        when(accountMapper.toResponseItem(testAccount)).thenReturn(accountListResponse);

        PaginatedResponse<AccountListResponse> response = accountService.getAllAccounts(Pageable.unpaged());

        assertNotNull(response);
        assertEquals(1, response.getTotalCount());
        assertEquals(1, response.getList().size());
    }

    @Test
    void updateAccount_Success() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(testAccount)).thenReturn(testAccount);

        SuccessResponse response = accountService.updateAccount(updateAccountRequest);

        assertNotNull(response);
        assertEquals("Account updated successfully", response.getMessage());
        verify(accountMapper, times(1)).updateEntity(testAccount, updateAccountRequest);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void updateAccount_NotFound() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, 
            () -> accountService.updateAccount(updateAccountRequest));

        assertEquals("Account not found with id: acc-123", exception.getMessage());
    }

    @Test
    void deleteAccount_Success() {
        when(accountRepository.existsById("acc-123")).thenReturn(true);

        assertDoesNotThrow(() -> accountService.deleteAccount("acc-123"));
        verify(accountRepository, times(1)).deleteById("acc-123");
    }

    @Test
    void deleteAccount_NotFound() {
        when(accountRepository.existsById("acc-123")).thenReturn(false);

        AccountException exception = assertThrows(AccountException.class, 
            () -> accountService.deleteAccount("acc-123"));

        assertEquals("Account not found with id: acc-123", exception.getMessage());
    }

    @Test
    void isAccountEligible_ActiveAccount_DebitTransaction_SufficientBalance() {
        testAccount.setStatus(AccountStatusEnum.ACTIVE);
        testAccount.setBalance(new BigDecimal("1000.00"));

        boolean result = accountService.isAccountEligible(testAccount, TransactionTypeEnum.D, new BigDecimal("500.00"));

        assertTrue(result);
    }

    @Test
    void isAccountEligible_ActiveAccount_DebitTransaction_InsufficientBalance() {
        testAccount.setStatus(AccountStatusEnum.ACTIVE);
        testAccount.setBalance(new BigDecimal("1000.00"));

        boolean result = accountService.isAccountEligible(testAccount, TransactionTypeEnum.D, new BigDecimal("1500.00"));

        assertFalse(result);
    }

    @Test
    void isAccountEligible_InactiveAccount() {
        testAccount.setStatus(AccountStatusEnum.INACTIVE);

        boolean result = accountService.isAccountEligible(testAccount, TransactionTypeEnum.D, new BigDecimal("500.00"));

        assertFalse(result);
    }

    @Test
    void isAccountEligible_CreditTransaction() {
        testAccount.setStatus(AccountStatusEnum.ACTIVE);

        boolean result = accountService.isAccountEligible(testAccount, TransactionTypeEnum.C, new BigDecimal("500.00"));

        assertTrue(result);
    }

    @Test
    void updateBalance_DebitTransaction() {
        testAccount.setBalance(new BigDecimal("1000.00"));
        Account updatedAccount = new Account();
        updatedAccount.setBalance(new BigDecimal("700.00"));
        
        when(accountRepository.save(testAccount)).thenReturn(updatedAccount);

        Account result = accountService.updateBalance(testAccount, TransactionTypeEnum.D, new BigDecimal("300.00"));

        assertEquals(new BigDecimal("700.00"), testAccount.getBalance());
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void updateBalance_CreditTransaction() {
        testAccount.setBalance(new BigDecimal("1000.00"));
        Account updatedAccount = new Account();
        updatedAccount.setBalance(new BigDecimal("1300.00"));
        
        when(accountRepository.save(testAccount)).thenReturn(updatedAccount);

        Account result = accountService.updateBalance(testAccount, TransactionTypeEnum.C, new BigDecimal("300.00"));

        assertEquals(new BigDecimal("1300.00"), testAccount.getBalance());
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void getAccountEntityById_Success() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.of(testAccount));

        Account result = accountService.getAccountEntityById("acc-123");

        assertNotNull(result);
        assertEquals("acc-123", result.getId());
    }

    @Test
    void getAccountEntityById_NotFound() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, 
            () -> accountService.getAccountEntityById("acc-123"));

        assertEquals("Account not found with id: acc-123", exception.getMessage());
    }
}