package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;

public class RegisterResponse extends BaseResponse{
    public RegisterResponse(Status status, String message) {
        super(status, message);
    }

    public RegisterResponse(Status status) {
        super(status);
    }
}
