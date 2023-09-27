package com.financeTracker.financeTracker.data.dtos;

import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
