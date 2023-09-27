package com.financeTracker.financeTracker.controller;

import com.financeTracker.financeTracker.data.dtos.*;
import com.financeTracker.financeTracker.services.TransactionService;
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

    @GetMapping("{uuid}")
    @PreAuthorize("hasAuthority('USER')")
    public FetchSingleTransactionResponse fetchTransaction(@PathVariable String uuid){
        return responseWithUpdatedHttpStatus(transactionService.fetchSingleTransaction(uuid));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER')")
    public FetchTransactionsResponse fetchAllTransactions(){
        return responseWithUpdatedHttpStatus(transactionService.fetchAllTransactions());
    }

    @GetMapping("category/{category}")
    @PreAuthorize("hasAuthority('USER')")
    public FetchTransactionsResponse fetchTransactionsByCategory(@PathVariable String category){
        return responseWithUpdatedHttpStatus(transactionService.fetchTransactionsByTransactionCategory(category));
    }

    @PatchMapping("{uuid}")
    @PreAuthorize("hasAuthority('USER')")
    public UpdateTransactionResponse updateTransaction(@RequestBody UpdateTransactionRequest request,@PathVariable String uuid){
        return responseWithUpdatedHttpStatus(transactionService.updateTransaction(request,uuid));
    }

    @DeleteMapping("{uuid}")
    @PreAuthorize("hasAuthority('USER')")
    public DeleteTransactionResponse deleteTransaction(@PathVariable String uuid){
        return responseWithUpdatedHttpStatus(transactionService.deleteTransactionByUUID(uuid));
    }
}
