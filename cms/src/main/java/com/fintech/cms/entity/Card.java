package com.fintech.cms.entity;

import com.fintech.cms.enums.CardStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatusEnum status;
    
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expiry;
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String cardNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    
}