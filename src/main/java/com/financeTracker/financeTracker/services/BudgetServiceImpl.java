package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.*;
import com.financeTracker.financeTracker.data.enums.Category;
import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.data.model.Budget;
import com.financeTracker.financeTracker.data.repositories.BudgetRepository;
import com.financeTracker.financeTracker.exceptions.*;
import com.financeTracker.financeTracker.security.CurrentUserService;
import com.financeTracker.financeTracker.utils.ApplicationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserService userService;
    @Override
    public AddBudgetResponse addBudget(AddBudgetRequest addBudgetRequest) {
        try{
            validateAddBudgetRequest(addBudgetRequest);
            validateBudgets(addBudgetRequest);
            String username  = CurrentUserService.getCurrentUserUsername();
            AppUser user = userService.findUserByUsername(username);
            Category category = ApplicationUtils.getTransactionCategoryType(addBudgetRequest.getBudgetCategory());
            checkIfUserAlreadyHasABudgetForTheCategoryForTheMonth(category,user.getId());
            UUID uuid = UUID.randomUUID();
            Budget budget = new Budget();
            budget.setUuid(uuid.toString());
            budget.setCategory(category);
            budget.setAppUser(user);
            budget.setMonthlyBudget(BigDecimal.valueOf(addBudgetRequest.getMonthlyBudget()));
            budget.setWeeklyBudget(BigDecimal.valueOf(addBudgetRequest.getWeeklyBudget()));
            budget.setTotalMonthlySpent(BigDecimal.ZERO);
            budget.setWeekOneAmount(BigDecimal.ZERO);
            budget.setWeekTwoAmount(BigDecimal.ZERO);
            budget.setWeekThreeAmount(BigDecimal.ZERO);
            budget.setWeekFourAmount(BigDecimal.ZERO);
            return new AddBudgetResponse(Status.CREATED,"Budget for "+category.name() +" has been added successfully",saveBudget(budget));
        }catch (InvalidUserInputException|InvalidTransactionCategory|BudgetException ex){
            return new AddBudgetResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        } catch (UserNotFoundException  e) {
            return new AddBudgetResponse(Status.UNAUTHORIZED,e.getLocalizedMessage());
        }
    }

    @Override
    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public FetchSingleBudgetResponse fetchSingleBudget(String uuid) {
        try{
            validateUUID(uuid);
            Budget budget = getBudgetByUUID(uuid);
            return new FetchSingleBudgetResponse(Status.SUCCESS,"Budget fetched successfully",budget);
        }catch (InvalidUserInputException|BudgetNotFoundException ex){
            return new FetchSingleBudgetResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        }
    }

    private void validateUUID(String uuid) throws InvalidUserInputException {
        if (null == uuid || uuid.isBlank() || uuid.isEmpty()){
            throw new InvalidUserInputException("UUID is required");
        }
    }

    @Override
    public Budget getBudgetByUUID(String uuid) throws BudgetNotFoundException {
        return budgetRepository.findBudgetByUuid(uuid).orElseThrow(()->new BudgetNotFoundException("Budget with uuid "+uuid+" not found!"));
    }

    @Override
    public FetchBudgetsResponse fetchAllBudgetForAMonthAndYear(int year, int month) {
        try{
            validateYearAndMonth(year,month);
            String username = CurrentUserService.getCurrentUserUsername();
            List<Budget> budgets = budgetRepository.findBudgetsByYearMonthAndUsername(year,month,username);
            return new FetchBudgetsResponse(Status.SUCCESS,"Budgets fetched successfully",budgets);
        }catch (InvalidUserInputException ex){
            return new FetchBudgetsResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        }
    }

    @Override
    public UpdateBudgetResponse editBudget(String uuid,UpdateBudgetRequest request) {
        try {
            validateUUID(uuid);
            validateUpdateBudgetRequest(request);
            Budget budget = getBudgetByUUID(uuid);
            budget.setWeeklyBudget(BigDecimal.valueOf(request.getWeeklyBudget()));
            budget.setMonthlyBudget(BigDecimal.valueOf(request.getMonthlyBudget()));
            return new UpdateBudgetResponse(Status.SUCCESS,"Budget updated successfully",saveBudget(budget));
        }catch (InvalidUserInputException |BudgetException ex){
            return new UpdateBudgetResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        }
    }

    @Override
    public DeleteBudgetResponse deleteBudget(String uuid) {
        try{
            validateUUID(uuid);
            Budget budget = getBudgetByUUID(uuid);
            budgetRepository.delete(budget);
            return new DeleteBudgetResponse(Status.SUCCESS,"Budget deleted successfully");
        }catch (InvalidUserInputException | BudgetNotFoundException ex){
            return new DeleteBudgetResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        }
    }

    @Override
    public void getCurrentBudgetAndUpdateSpending(Long userid,double amount,Category category) throws BudgetException {
        List<Budget> budgets =  budgetRepository.findBudgetByAppUser_IdAndCategory(userid,category);
        Budget budget = budgets.stream()
                .max(Comparator.comparing(Budget::getCreatedAt))
                .orElse(null);
        log.info("Budget {}",budget);
        if (null!= budget){
            log.info("Total monthly spent {}",budget.getTotalMonthlySpent());
            budget.setTotalMonthlySpent(budget.getTotalMonthlySpent().add(BigDecimal.valueOf(amount)));
            log.info("Total monthly sepnt after {}",budget.getTotalMonthlySpent());
            setWeekAmount(budget,amount);
        }else throw new BudgetException("Add budget for category before adding a transaction");
    }

    private int calculateWeekOfTheMonth(){
        LocalDate currentDate = LocalDate.now();
        return (currentDate.getDayOfMonth() - 1) / 7 + 1;
    }

    private void setWeekAmount(Budget budget, double amount){
        int week = calculateWeekOfTheMonth();
        if (week == 1){
            budget.setWeekOneAmount(budget.getWeekOneAmount().add(BigDecimal.valueOf(amount)));
        }
        if (week == 2){
            budget.setWeekTwoAmount(budget.getWeekTwoAmount().add(BigDecimal.valueOf(amount)));
        }
        if (week == 3){
            budget.setWeekThreeAmount(budget.getWeekThreeAmount().add(BigDecimal.valueOf(amount)));
        }
        if (week == 4){
            budget.setWeekFourAmount(budget.getWeekFourAmount().add(BigDecimal.valueOf(amount)));
        }
        saveBudget(budget);
    }
    private void validateUpdateBudgetRequest(UpdateBudgetRequest updateBudgetRequest) throws InvalidUserInputException, BudgetException {
        if (updateBudgetRequest.getWeeklyBudget()<=0){
            throw new InvalidUserInputException("Weekly budget cannot be less than zero");
        }

        if (updateBudgetRequest.getMonthlyBudget()<=0){
            throw new InvalidUserInputException("Monthly budget cannot be less than zero");
        }
        if (updateBudgetRequest.getWeeklyBudget() > updateBudgetRequest.getMonthlyBudget()){
            throw new BudgetException("weekly budget cannot be greater than monthly budget");
        }
        if (updateBudgetRequest.getMonthlyBudget() == updateBudgetRequest.getWeeklyBudget()){
            throw new BudgetException("Monthly and weekly budget cannot be equal");
        }

        if (updateBudgetRequest.getMonthlyBudget()/4 != updateBudgetRequest.getWeeklyBudget()){
            throw new BudgetException("Please make sure the monthly budget and the weekly budget is correct");
        }
    }
    private void validateYearAndMonth(int year, int month) throws InvalidUserInputException {
        if (year < 2023){
            throw new InvalidUserInputException("Year is invalid");
        }
        if (month<=0 || month >12){
            throw new InvalidUserInputException("Month is invalid");
        }
    }

    private void checkIfUserAlreadyHasABudgetForTheCategoryForTheMonth(Category budgetCategory, Long userId) throws BudgetException {
        List<Budget> budgets =  budgetRepository.findBudgetByAppUser_IdAndCategory(userId,budgetCategory);
        Budget budget = budgets.stream()
                .max(Comparator.comparing(Budget::getCreatedAt))
                .orElse(null);
        if (null != budget){
            if (budget.getCreatedAt().getYear() == new Date().getYear() && budget.getCreatedAt().getMonth() == new Date().getMonth()){
                throw new BudgetException("budget for category "+budgetCategory.name()+" has already been created for this month");
            }
        }
    }
    private void validateAddBudgetRequest(AddBudgetRequest request) throws InvalidUserInputException {
        if (null == request.getBudgetCategory() || request.getBudgetCategory().isEmpty() || request.getBudgetCategory().isBlank()){
            throw new InvalidUserInputException("Budget category is required");
        }
        if (request.getWeeklyBudget()<=0){
            throw new InvalidUserInputException("Weekly budget cannot be less than zero");
        }

        if (request.getMonthlyBudget()<=0){
            throw new InvalidUserInputException("Monthly budget cannot be less than zero");
        }

    }

    private void validateBudgets(AddBudgetRequest addBudgetRequest) throws BudgetException {
        if (addBudgetRequest.getWeeklyBudget() > addBudgetRequest.getMonthlyBudget()){
            throw new BudgetException("weekly budget cannot be greater than monthly budget");
        }
        if (addBudgetRequest.getMonthlyBudget() == addBudgetRequest.getWeeklyBudget()){
            throw new BudgetException("Monthly and weekly budget cannot be equal");
        }

        if (addBudgetRequest.getMonthlyBudget()/4 != addBudgetRequest.getWeeklyBudget()){
            throw new BudgetException("Please make sure the monthly budget and the weekly budget is correct");
        }
    }
}
