package com.financeTracker.financeTracker.controller;

import com.financeTracker.financeTracker.data.dtos.*;
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

    @GetMapping("{refreshToken}")
    public ReGenerateTokenResponse reGenerateToken(@PathVariable String refreshToken){
        return responseWithUpdatedHttpStatus(authService.regenerateToken(refreshToken));
    }
}
