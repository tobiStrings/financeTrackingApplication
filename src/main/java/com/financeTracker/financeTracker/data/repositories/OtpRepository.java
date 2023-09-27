package com.financeTracker.financeTracker.data.repositories;

import com.financeTracker.financeTracker.data.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {
    Optional<Otp> findOtpByOtp(String otp);
}
