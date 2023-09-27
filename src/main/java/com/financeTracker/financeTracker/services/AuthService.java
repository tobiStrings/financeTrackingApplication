package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.LoginRequest;
import com.financeTracker.financeTracker.data.dtos.LoginResponse;
import com.financeTracker.financeTracker.data.dtos.RegisterRequest;
import com.financeTracker.financeTracker.data.dtos.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest request);
}
