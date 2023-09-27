package com.financeTracker.financeTracker.data.dtos;

import lombok.Data;

@Data
public class AddTransactionRequest {
    private double amount;
    private String transactionCategory;
}
