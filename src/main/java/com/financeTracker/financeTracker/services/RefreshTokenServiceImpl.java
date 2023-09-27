package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.data.model.RefreshToken;
import com.financeTracker.financeTracker.data.repositories.RefreshTokenRepository;
import com.financeTracker.financeTracker.exceptions.RefreshTokenException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateRefreshToken(AppUser userId){
        try{
            RefreshToken refreshToken = findRefreshTokenByAppUserId(userId.getId());

            refreshToken.setCreatedDate(Instant.now());
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setAppUser(userId);
            refreshToken.setExpirationDate(Instant.ofEpochMilli(1800000));
            return saveRefreshToken(refreshToken);
        }catch (RefreshTokenException ex){

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setCreatedDate(Instant.now());
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setAppUser(userId);
            refreshToken.setExpirationDate(Instant.ofEpochMilli(1800000));
            return saveRefreshToken(refreshToken);
        }
    }
    private RefreshToken saveRefreshToken(RefreshToken refreshToken){
        return refreshTokenRepository.save(refreshToken);
    }
    @Override
    public void validateRefreshToken(String refreshToken) throws RefreshTokenException {
        refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));
    }

    @Override
    public void deleteRefreshToken(String refreshToken){
        refreshTokenRepository.deleteRefreshTokenByToken(refreshToken);
    }

    @Override
    public RefreshToken findRefreshTokenByRefreshToken(String token) throws RefreshTokenException {
        Optional<RefreshToken> foundToken = refreshTokenRepository.findByToken(token);
        if (foundToken.isEmpty()){
            throw new RefreshTokenException("Token not found");
        }
        return foundToken.get();
    }

    @Override
    public boolean checkRefreshTokenExpiration(String refreshToken) throws RefreshTokenException {
        RefreshToken foundToken = findRefreshTokenByRefreshToken(refreshToken);
        return foundToken.getCreatedDate().isBefore(foundToken.getExpirationDate());
    }

    @Override
    public RefreshToken findRefreshTokenByAppUserId(Long id) throws RefreshTokenException {
        return refreshTokenRepository.findRefreshTokenByAppUser_Id(id).orElseThrow(() -> new RefreshTokenException("Refresh token not found"));
    }
}
