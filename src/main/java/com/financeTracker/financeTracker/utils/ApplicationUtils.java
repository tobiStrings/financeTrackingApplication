package com.financeTracker.financeTracker.utils;

import com.financeTracker.financeTracker.data.enums.Category;
import com.financeTracker.financeTracker.exceptions.InvalidTransactionCategory;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUtils {
    public static Category getTransactionCategoryType(String transactionCategory) throws InvalidTransactionCategory {
        if (transactionCategory.equalsIgnoreCase("UTILITY")){
            return Category.UTILITY;
        }

        if (transactionCategory.equalsIgnoreCase("FOOD")){
            return Category.FOOD;
        }

        if (transactionCategory.equalsIgnoreCase("HOUSING")){
            return Category.HOUSING;
        }

        if (transactionCategory.equalsIgnoreCase("OTHERS")){
            return Category.OTHERS;
        }
        throw new InvalidTransactionCategory("Invalid category");
    }
}
