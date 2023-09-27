package com.financeTracker.financeTracker.data.repositories;

import com.financeTracker.financeTracker.data.enums.TransactionCategory;
import com.financeTracker.financeTracker.data.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findTransactionByUuid(String uuid);
    List<Transaction> findTransactionByAppUser_Username(String username);
    List<Transaction> findTransactionByTransactionCategoryAndAppUser_Username(TransactionCategory transactionCategory,String username);
}
