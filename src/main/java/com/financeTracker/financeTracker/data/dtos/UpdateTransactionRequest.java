package com.financeTracker.financeTracker.data.dtos;

import lombok.Data;

@Data
public class UpdateTransactionRequest {
    private String transactionCategory;
    private double amount;
}
