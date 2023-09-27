package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.*;

public interface AuthService {

    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest request);
    ReGenerateTokenResponse regenerateToken(String refreshToken);
}
