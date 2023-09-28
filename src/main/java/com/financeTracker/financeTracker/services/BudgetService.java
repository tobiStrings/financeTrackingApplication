package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.*;
import com.financeTracker.financeTracker.data.enums.Category;
import com.financeTracker.financeTracker.data.model.Budget;
import com.financeTracker.financeTracker.exceptions.BudgetException;
import com.financeTracker.financeTracker.exceptions.BudgetNotFoundException;

public interface BudgetService {
    AddBudgetResponse addBudget(AddBudgetRequest addBudgetRequest);
    Budget saveBudget(Budget budget);

    FetchSingleBudgetResponse fetchSingleBudget(String uuid);

    Budget getBudgetByUUID(String uuid) throws BudgetNotFoundException;

    FetchBudgetsResponse fetchAllBudgetForAMonthAndYear(int year, int month);
    UpdateBudgetResponse editBudget(String uuid,UpdateBudgetRequest request);
    DeleteBudgetResponse deleteBudget(String uuid);
    void getCurrentBudgetAndUpdateSpending(Long userid,double amount, Category category) throws BudgetException;
}
