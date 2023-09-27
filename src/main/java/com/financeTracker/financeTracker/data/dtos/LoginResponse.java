package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
public class LoginResponse extends BaseResponse {
    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;
    private Long userId;
    public LoginResponse(Status status, String message) {
        super(status, message);
    }

    public LoginResponse(Status status, String message,Long userid, String accessToken, String refreshToken, Instant expiresAt){
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.userId = userid;

    }

}
