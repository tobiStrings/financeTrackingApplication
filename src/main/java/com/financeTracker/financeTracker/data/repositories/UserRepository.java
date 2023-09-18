package com.financeTracker.financeTracker.data.repositories;

import com.financeTracker.financeTracker.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUSerByUsername(String username);

    Optional<User> findUserByEmailOrUsername(String email, String username);
}
