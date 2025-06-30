package com.fintech.cms.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.cms.dto.PaginatedResponse;
import com.fintech.cms.dto.account.AccountDetailsResponse;
import com.fintech.cms.dto.account.AccountListResponse;
import com.fintech.cms.dto.account.CreateAccountRequest;
import com.fintech.cms.dto.account.UpdateAccountRequest;
import com.fintech.cms.dto.common.SuccessResponse;
import com.fintech.cms.exception.AccountException;
import com.fintech.cms.service.impl.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateAccountRequest createAccountRequest;
    private UpdateAccountRequest updateAccountRequest;
    private AccountDetailsResponse accountDetailsResponse;
    private AccountListResponse accountListResponse;
    private SuccessResponse successResponse;
    private PaginatedResponse<AccountListResponse> paginatedResponse;

    @BeforeEach
    void setUp() {
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

        successResponse = new SuccessResponse("Account created successfully");

        paginatedResponse = new PaginatedResponse<>(Arrays.asList(accountListResponse), 1L);
    }

    @Test
    void createAccount_Success() throws Exception {
        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Account created successfully"));

        verify(accountService, times(1)).createAccount(any(CreateAccountRequest.class));
    }

    @Test
    void createAccount_ValidationError() throws Exception {
        createAccountRequest.setName(null);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).createAccount(any(CreateAccountRequest.class));
    }

    @Test
    void getAccountById_Success() throws Exception {
        when(accountService.getAccountById("acc-123")).thenReturn(accountDetailsResponse);

        mockMvc.perform(get("/api/accounts/{id}", "acc-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("acc-123"))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(accountService, times(1)).getAccountById("acc-123");
    }

    @Test
    void getAccountById_NotFound() throws Exception {
        when(accountService.getAccountById("acc-123")).thenThrow(new AccountException("Account not found"));

        mockMvc.perform(get("/api/accounts/{id}", "acc-123"))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).getAccountById("acc-123");
    }

    @Test
    void getAllAccounts_Success() throws Exception {
        when(accountService.getAllAccounts(any(Pageable.class))).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/accounts")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].id").value("acc-123"))
                .andExpect(jsonPath("$.totalCount").value(1));

        verify(accountService, times(1)).getAllAccounts(any(Pageable.class));
    }

    @Test
    void updateAccount_ValidationError() throws Exception {
        updateAccountRequest.setId(null);

        mockMvc.perform(put("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).updateAccount(any(UpdateAccountRequest.class));
    }

    @Test
    void deleteAccount_Success() throws Exception {
        doNothing().when(accountService).deleteAccount("acc-123");

        mockMvc.perform(delete("/api/accounts/{id}", "acc-123"))
                .andExpect(status().isOk());

        verify(accountService, times(1)).deleteAccount("acc-123");
    }

    @Test
    void deleteAccount_NotFound() throws Exception {
        doThrow(new AccountException("Account not found")).when(accountService).deleteAccount("acc-123");

        mockMvc.perform(delete("/api/accounts/{id}", "acc-123"))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).deleteAccount("acc-123");
    }
}