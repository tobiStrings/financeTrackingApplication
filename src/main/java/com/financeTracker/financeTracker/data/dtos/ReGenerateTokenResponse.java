package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import lombok.Data;

@Data
public class ReGenerateTokenResponse extends BaseResponse{
    private String accessToken;
    private String refreshToken;
    public ReGenerateTokenResponse(Status status, String message) {
        super(status, message);
    }

    public ReGenerateTokenResponse(Status status, String message,String accessToken,String refreshToken) {
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
