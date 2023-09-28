package com.financeTracker.financeTracker.controller;

import com.financeTracker.financeTracker.data.dtos.*;
import com.financeTracker.financeTracker.services.BudgetService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/budget/")
public class BudgetController extends Controller{
    private final BudgetService budgetService;

    @PostMapping("add")
    @PreAuthorize("hasAuthority('USER')")
    public AddBudgetResponse addBudget(@RequestBody AddBudgetRequest addBudgetRequest){
        return responseWithUpdatedHttpStatus(budgetService.addBudget(addBudgetRequest));
    }

    @GetMapping("{uuid}")
    @PreAuthorize("hasAuthority('USER')")
    public FetchSingleBudgetResponse fetchSingleBudget(@PathVariable String uuid){
        return responseWithUpdatedHttpStatus(budgetService.fetchSingleBudget(uuid));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER')")
    public FetchBudgetsResponse fetchBudgetsByMonthAndYear(@RequestParam(value = "year") int year, @RequestParam(value = "month") int month){
        return responseWithUpdatedHttpStatus(budgetService.fetchAllBudgetForAMonthAndYear(year,month));
    }

    @PatchMapping("{uuid}")
    @PreAuthorize("hasAuthority('USER')")
    public UpdateBudgetResponse updateBudget(@RequestBody UpdateBudgetRequest updateBudgetRequest, @PathVariable String uuid){
        return responseWithUpdatedHttpStatus(budgetService.editBudget(uuid,updateBudgetRequest));
    }

    @DeleteMapping("{uuid}")
    @PreAuthorize("hasAuthority('USER')")
    public DeleteBudgetResponse deleteBudget(@PathVariable String uuid){
        return responseWithUpdatedHttpStatus(budgetService.deleteBudget(uuid));
    }
}
