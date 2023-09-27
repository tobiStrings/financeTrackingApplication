package com.financeTracker.financeTracker.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financeTracker.financeTracker.data.enums.TransactionCategory;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal amount;
    private String uuid;
    @Enumerated(EnumType.STRING)
    private TransactionCategory transactionCategory;
    @ManyToOne
    @JsonIgnore
    private AppUser appUser;
    @CreationTimestamp
    private Date createdAT;
    @UpdateTimestamp
    private Date updatedAt;
}
