package com.financeTracker.financeTracker.controller;

import com.financeTracker.financeTracker.data.dtos.AddTransactionRequest;
import com.financeTracker.financeTracker.data.dtos.AddTransactionResponse;
import com.financeTracker.financeTracker.services.TransactionService;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions/")
@AllArgsConstructor
public class TransactionController extends Controller{
    private final TransactionService transactionService;

    @PostMapping("add")
    @PreAuthorize("hasAuthority('USER')")
    public AddTransactionResponse addTransaction(@RequestBody  AddTransactionRequest request){
        return responseWithUpdatedHttpStatus(transactionService.addTransaction(request));
    }
}
