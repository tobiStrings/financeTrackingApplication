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
import com.financeTracker.financeTracker.security.CurrentUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
            Transaction transaction = new Transaction();
            transaction.setTransactionCategory(getTransactionCategoryType(addTransactionRequest.getTransactionCategory()));
            transaction.setAmount(BigDecimal.valueOf(addTransactionRequest.getAmount()));
            transaction.setCreatedAT(new Date());
            AppUser user = userService.findUserByUsername(CurrentUserService.getCurrentUserUsername());
            transaction.setAppUser(user);
            return new AddTransactionResponse(Status.SUCCESS,"Transaction added successfully",saveTransaction(transaction));
        }catch (InvalidUserInputException |InvalidTransactionCategory ex){
            return new AddTransactionResponse(Status.BAD_REQUEST,ex.getMessage(),null);
        }catch (UserNotFoundException e){
            return new AddTransactionResponse(Status.UNAUTHORIZED,e.getLocalizedMessage(),null);
        }
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    private void validateAddTransactionRequest(AddTransactionRequest request) throws InvalidUserInputException {
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
        throw new InvalidTransactionCategory("Invalid transaction category");
    }
}
