package com.fintech.cms.entity;

import com.fintech.cms.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @NotNull
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal transactionAmount;
    
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionTypeEnum transactionType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    
}