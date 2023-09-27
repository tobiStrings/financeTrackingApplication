package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.AddTransactionRequest;
import com.financeTracker.financeTracker.data.dtos.AddTransactionResponse;
import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.enums.TransactionCategory;
import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.data.model.Transaction;
import com.financeTracker.financeTracker.data.repositories.TransactionRepository;
import com.financeTracker.financeTracker.exceptions.InvalidTransactionCategory;
import com.financeTracker.financeTracker.exceptions.InvalidUserInputException;
import com.financeTracker.financeTracker.exceptions.TransactionException;
import com.financeTracker.financeTracker.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    @Override
    public AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest) {
        try{
            validateAddTransactionRequest(addTransactionRequest);
            AppUser user = userService.findUserByEMail(addTransactionRequest.getUserEmail());
            Transaction transaction = new Transaction();
            transaction.setTransactionCategory(getTransactionCategoryType(addTransactionRequest.getTransactionCategory()));
            transaction.setAmount(BigDecimal.valueOf(addTransactionRequest.getAmount()));
            transaction.setCreatedAT(new Date());
            transaction.setAppUser(user);
            return new AddTransactionResponse(Status.SUCCESS,"Transaction added sucessfully",saveTransaction(transaction));
        }catch (InvalidUserInputException | UserNotFoundException|InvalidTransactionCategory ex){
            return new AddTransactionResponse(Status.BAD_REQUEST,ex.getMessage(),null);
        }
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    private void validateAddTransactionRequest(AddTransactionRequest request) throws InvalidUserInputException {
        if (null ==  request.getUserEmail() || request.getUserEmail().isBlank() ||request.getUserEmail().isEmpty()){
            throw new InvalidUserInputException("User email cannot be blank or empty");
        }
        if (request.getAmount() <=0){
            throw new InvalidUserInputException("Transaction amount cannot be recorded");
        }
        if (null == request.getTransactionCategory()|| request.getTransactionCategory().isEmpty() || request.getTransactionCategory().isBlank()){
            throw new InvalidUserInputException("Transaction category cannot be empty or blank");
        }
    }

    private TransactionCategory getTransactionCategoryType(String transactionCategory) throws InvalidTransactionCategory {
        if (transactionCategory.equalsIgnoreCase("UTILITY")){
            return TransactionCategory.UTILITY;
        }

        if (transactionCategory.equalsIgnoreCase("FOOD")){
            return TransactionCategory.FOOD;
        }

        if (transactionCategory.equalsIgnoreCase("HOUSING")){
            return TransactionCategory.HOUSING;
        }

        if (transactionCategory.equalsIgnoreCase("OTHERS")){
            return TransactionCategory.OTHERS;
        }
        throw new InvalidTransactionCategory();
    }
}
