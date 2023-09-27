package com.financeTracker.financeTracker.controller;

import com.financeTracker.financeTracker.data.dtos.BaseResponse;
import com.financeTracker.financeTracker.data.dtos.LoginRequest;
import com.financeTracker.financeTracker.data.dtos.LoginResponse;
import com.financeTracker.financeTracker.data.dtos.RegisterRequest;
import com.financeTracker.financeTracker.services.AuthService;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@AllArgsConstructor
public class AuthController extends com.financeTracker.financeTracker.controller.Controller {
    private final AuthService authService;
    @PostMapping("register")
    @PermitAll
    public BaseResponse register(@RequestBody RegisterRequest request){
         return responseWithUpdatedHttpStatus(authService.register(request));
    }

    @PostMapping("login")
    @PermitAll
    public LoginResponse login(@RequestBody LoginRequest request){
        return responseWithUpdatedHttpStatus(authService.login(request));
    }
}
