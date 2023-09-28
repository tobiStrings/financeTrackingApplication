package com.financeTracker.financeTracker.data.repositories;

import com.financeTracker.financeTracker.data.enums.Category;
import com.financeTracker.financeTracker.data.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long> {
    List<Budget> findBudgetByAppUser_IdAndCategory(Long id, Category category);
    Optional<Budget> findBudgetByUuid(String uuid);

    @Query("SELECT b FROM Budget b WHERE YEAR(b.createdAt) = :year AND MONTH(b.createdAt) = :month")
    List<Budget> findBudgetsByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT b FROM Budget b WHERE YEAR(b.createdAt) = :year AND MONTH(b.createdAt) = :month AND b.appUser.username = :username")
    List<Budget> findBudgetsByYearMonthAndUsername(
            @Param("year") int year,
            @Param("month") int month,
            @Param("username") String username
    );
}
