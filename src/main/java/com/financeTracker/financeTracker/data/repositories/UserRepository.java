package com.financeTracker.financeTracker.data.repositories;

import com.financeTracker.financeTracker.data.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findUserByEmail(String email);
    Optional<AppUser> findUSerByUsername(String username);

    Optional<AppUser> findUserByEmailOrUsername(String email, String username);
}
