package com.fintech.cms.entity;

import com.fintech.cms.enums.AccountStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatusEnum status;
    
    @NotNull
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Card> cards;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
    
}