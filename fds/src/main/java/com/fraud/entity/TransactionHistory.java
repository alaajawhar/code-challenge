package com.fraud.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "card_number", nullable = false)
    private String cardNumber;
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    
    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;
} 