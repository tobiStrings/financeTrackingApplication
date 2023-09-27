package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.model.Otp;
import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.exceptions.OtpException;

public interface OtpService {
    Otp generateOtp(AppUser appUser) throws OtpException;

    boolean verifyOtp(String otp);

    Otp findOtpByOtp(String otp) throws OtpException;

    void deleteOtp(Otp otp);
}
