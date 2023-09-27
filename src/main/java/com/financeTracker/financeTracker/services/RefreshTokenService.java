package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.data.model.RefreshToken;
import com.financeTracker.financeTracker.exceptions.RefreshTokenException;

public interface RefreshTokenService {
    RefreshToken generateRefreshToken(AppUser userId);

    void validateRefreshToken(String refreshToken) throws RefreshTokenException;

    void deleteRefreshToken(String refreshToken);

    RefreshToken findRefreshTokenByRefreshToken(String token) throws RefreshTokenException;

    boolean checkRefreshTokenExpiration(String refreshToken) throws RefreshTokenException;
}
