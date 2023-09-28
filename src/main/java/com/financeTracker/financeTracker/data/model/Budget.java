package com.financeTracker.financeTracker.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financeTracker.financeTracker.data.enums.Category;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uuid;
    @Enumerated(EnumType.STRING)
    private Category category;
    private BigDecimal monthlyBudget;
    private BigDecimal weeklyBudget;
    private BigDecimal totalMonthlySpent;
    private BigDecimal weekOneAmount;
    private BigDecimal weekTwoAmount;
    private BigDecimal weekThreeAmount;
    private BigDecimal weekFourAmount;
    @ManyToOne
    @JsonIgnore
    private AppUser appUser;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
}