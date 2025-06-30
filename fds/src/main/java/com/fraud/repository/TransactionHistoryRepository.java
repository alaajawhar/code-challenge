package com.fraud.repository;

import com.fraud.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, UUID> {
    
    @Query("SELECT COUNT(t) FROM TransactionHistory t WHERE t.cardNumber = :cardNumber AND t.transactionTime > :cutoffTime")
    int countByCardNumberAndTransactionTimeAfter(@Param("cardNumber") String cardNumber, 
                                                @Param("cutoffTime") LocalDateTime cutoffTime);
} 