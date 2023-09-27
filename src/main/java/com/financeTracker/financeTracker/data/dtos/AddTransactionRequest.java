package com.financeTracker.financeTracker.data.dtos;

import lombok.Data;

@Data
public class AddTransactionRequest {
    private String userEmail;
    private double amount;
    private String transactionCategory;
}
