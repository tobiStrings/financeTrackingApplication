package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.*;
import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.enums.Category;
import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.data.model.Transaction;
import com.financeTracker.financeTracker.data.repositories.TransactionRepository;
import com.financeTracker.financeTracker.exceptions.*;
import com.financeTracker.financeTracker.security.CurrentUserService;
import com.financeTracker.financeTracker.utils.ApplicationUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final BudgetService budgetService;
    @Override
    public AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest) {
        try{
            validateAddTransactionRequest(addTransactionRequest);
            Category category = ApplicationUtils.getTransactionCategoryType(addTransactionRequest.getTransactionCategory());
            UUID uuid = UUID.randomUUID();
            Transaction transaction = new Transaction();
            transaction.setUuid(uuid.toString());
            transaction.setTransactionCategory(category);
            transaction.setAmount(BigDecimal.valueOf(addTransactionRequest.getAmount()));
            transaction.setCreatedAT(new Date());
            AppUser user = userService.findUserByUsername(CurrentUserService.getCurrentUserUsername());
            transaction.setAppUser(user);
            budgetService.getCurrentBudgetAndUpdateSpending(user.getId(),addTransactionRequest.getAmount(),category);
            return new AddTransactionResponse(Status.SUCCESS,"Transaction added successfully",saveTransaction(transaction));
        }catch (InvalidUserInputException |InvalidTransactionCategory|BudgetException ex){
            return new AddTransactionResponse(Status.BAD_REQUEST,ex.getMessage(),null);
        }catch (UserNotFoundException e){
            return new AddTransactionResponse(Status.UNAUTHORIZED,e.getLocalizedMessage(),null);
        }
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public FetchSingleTransactionResponse fetchSingleTransaction(String uuid) {
        try{
            if (null == uuid|| uuid.isBlank() || uuid.isEmpty()){
                throw new InvalidUserInputException("Uuid is required");
            }
            Optional<Transaction> transaction = transactionRepository.findTransactionByUuid(uuid);
            if (transaction.isPresent()){
                return new FetchSingleTransactionResponse(Status.SUCCESS,"Transaction fetched successfully",transaction.get());
            }
            throw new TransactionNotFoundException("Transaction with UUid not found!");
        }catch (InvalidUserInputException ex){
            return new FetchSingleTransactionResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        } catch (TransactionNotFoundException e) {
            return new FetchSingleTransactionResponse(Status.UNAUTHORIZED,e.getLocalizedMessage());
        }
    }

    @Override
    public FetchTransactionsResponse fetchAllTransactions() {
        try{
            String currentUserUsername = CurrentUserService.getCurrentUserUsername();
            List<Transaction> transactions = transactionRepository.findTransactionByAppUser_Username(currentUserUsername);
            if (!transactions.isEmpty()){
                return new FetchTransactionsResponse(Status.SUCCESS,"All transaction fetched successfully",transactions);
            }
            throw new TransactionNotFoundException("User does not have transactions");
        }catch (TransactionNotFoundException e) {
           return new FetchTransactionsResponse(Status.BAD_REQUEST,e.getLocalizedMessage());
        }

    }

    @Override
    public FetchTransactionsResponse fetchTransactionsByTransactionCategory(String category) {
        try{
            String username  = CurrentUserService.getCurrentUserUsername();
            Category transactionCategory  = ApplicationUtils.getTransactionCategoryType(category);
            List<Transaction> transactions = transactionRepository.findTransactionByTransactionCategoryAndAppUser_Username(transactionCategory,username);
            if (transactions.isEmpty()){
                throw new TransactionNotFoundException("Transactions with category "+category +" does not exist");
            }
            return new FetchTransactionsResponse(Status.SUCCESS,"Transactions fetched successfully",transactions);
        }catch (InvalidTransactionCategory|TransactionNotFoundException ex){
            return new FetchTransactionsResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        }
    }

    @Override
    public UpdateTransactionResponse updateTransaction(UpdateTransactionRequest request,String uuid) {
        try {
            validateUpdateTransactionRequest(request,uuid);
            Transaction transaction = findTransactionByUuid(uuid);
            if (null != request.getTransactionCategory() && !request.getTransactionCategory().isEmpty() && !request.getTransactionCategory().isBlank()){
                transaction.setTransactionCategory(ApplicationUtils.getTransactionCategoryType(request.getTransactionCategory()));
            }
            if (request.getAmount() >0){
                transaction.setAmount(BigDecimal.valueOf(request.getAmount()));
            }
            transaction.setUpdatedAt(new Date());
            return new UpdateTransactionResponse(Status.SUCCESS,"Transaction update was successful",saveTransaction(transaction));
        }catch (InvalidUserInputException | TransactionNotFoundException|InvalidTransactionCategory ex){
            return new UpdateTransactionResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        }
    }
    @Override
    public Transaction findTransactionByUuid(String uuid) throws TransactionNotFoundException {
        return transactionRepository.findTransactionByUuid(uuid).orElseThrow(()-> new TransactionNotFoundException("Transaction not found"));
    }

    @Override
    public DeleteTransactionResponse deleteTransactionByUUID(String uuid) {
        try {
            if (null == uuid || uuid.isBlank() || uuid.isEmpty()){
                throw new InvalidUserInputException("UUID is required");
            }
            Transaction transaction = findTransactionByUuid(uuid);
            transactionRepository.delete(transaction);
            return new DeleteTransactionResponse(Status.SUCCESS,"Transaction deleted successfully");
        }catch (InvalidUserInputException | TransactionNotFoundException ex){
            return new DeleteTransactionResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        }
    }

    private void validateUpdateTransactionRequest(UpdateTransactionRequest request,String uuid) throws InvalidUserInputException {
        if (null == uuid || uuid.isEmpty() || uuid.isBlank()){
            throw new InvalidUserInputException("UUID is required for update");
        }

        if ((request.getAmount() <= 0) && (null == request.getTransactionCategory() || request.getTransactionCategory().isBlank() || request.getTransactionCategory().isEmpty())){
            throw new InvalidUserInputException("Amount or transaction category is required for update");
        }
    }
    private void validateAddTransactionRequest(AddTransactionRequest request) throws InvalidUserInputException {
        if (request.getAmount() <=0){
            throw new InvalidUserInputException("Transaction amount cannot be recorded");
        }
        if (null == request.getTransactionCategory()|| request.getTransactionCategory().isEmpty() || request.getTransactionCategory().isBlank()){
            throw new InvalidUserInputException("Transaction category cannot be empty or blank");
        }
    }

}
