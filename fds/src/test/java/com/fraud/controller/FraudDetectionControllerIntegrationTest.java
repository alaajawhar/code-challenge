package com.fraud.controller;

import com.fraud.dto.FraudCheckRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FraudDetectionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnFraudForHighAmount() throws Exception {
        FraudCheckRequest req = new FraudCheckRequest("4111111111111111", new java.math.BigDecimal("20000"));

        mockMvc.perform(post("/api/fraud/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fraud").value(true))
                .andExpect(jsonPath("$.reason").value("Transaction amount exceeds the fraud limit of $10,000"));
    }

    @Test
    void shouldReturnNotFraudForNormalAmount() throws Exception {
        FraudCheckRequest req = new FraudCheckRequest("4111111111111111", new java.math.BigDecimal("100"));

        mockMvc.perform(post("/api/fraud/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fraud").value(false))
                .andExpect(jsonPath("$.reason").value("Transaction passed all fraud checks"));
    }

    @Test
    void shouldReturnFraudForExceedingFrequency() throws Exception {
        String cardNumber = "4000000000000002";
        // Insert 8 transactions for the same card in the last hour
        for (int i = 0; i < 8; i++) {
            FraudCheckRequest req = new FraudCheckRequest(cardNumber, new java.math.BigDecimal("10"));
            mockMvc.perform(post("/api/fraud/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                    .andExpect(jsonPath("$.fraud").value(false))
                    .andExpect(status().isOk());
        }
        // The 9th transaction should be flagged as fraud
        FraudCheckRequest req = new FraudCheckRequest(cardNumber, new java.math.BigDecimal("10"));
        mockMvc.perform(post("/api/fraud/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fraud").value(true))
                .andExpect(jsonPath("$.reason").value(org.hamcrest.Matchers.containsString("Too many transactions")));
    }

    @Test
    void shouldReturnNotFraudForNormalFrequency() throws Exception {
        String cardNumber = "4000000000000003";
        // Insert 8 transactions for the same card in the last hour
        for (int i = 0; i < 8; i++) {
            FraudCheckRequest req = new FraudCheckRequest(cardNumber, new java.math.BigDecimal("10"));
            mockMvc.perform(post("/api/fraud/check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(jsonPath("$.fraud").value(false))
                    .andExpect(status().isOk());
        }
    }
} 