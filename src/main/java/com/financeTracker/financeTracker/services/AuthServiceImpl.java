package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.LoginRequest;
import com.financeTracker.financeTracker.data.dtos.LoginResponse;
import com.financeTracker.financeTracker.data.model.Otp;
import com.financeTracker.financeTracker.data.model.RefreshToken;
import com.financeTracker.financeTracker.exceptions.*;
import com.financeTracker.financeTracker.security.CustomAuthenticationManager;
import com.financeTracker.financeTracker.security.JwtTokenProvider;
import com.financeTracker.financeTracker.utils.Constant;
import com.financeTracker.financeTracker.data.dtos.RegisterRequest;
import com.financeTracker.financeTracker.data.dtos.RegisterResponse;
import com.financeTracker.financeTracker.data.enums.Role;
import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.utils.EmailUtils;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailUtils emailUtils;
//    private final AuthenticationManager authenticateManager;
    private final CustomAuthenticationManager customAuthenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        try{
            validateRegisterRequest(registerRequest);
            checkIfUserWithEmailOrUsernameExist(registerRequest.getEmail(), registerRequest.getUsername());
            AppUser appUser = AppUser.builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .username(registerRequest.getUsername())
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .role(Role.USER)
                    .enabled(false)
                    .createdDate(new Date())
                    .build();
            appUser = userService.saveUser(appUser);
            Otp otp = otpService.generateOtp(appUser);
            emailUtils.sendRegistrationMail(appUser.getFirstName(), appUser.getEmail(),otp.getOtp());
            log.info("Here before returning response!");
            return new RegisterResponse(Status.CREATED,"User created Successfully");
        }catch (InvalidUserInputException| UserAlreadyExistException ex){
            log.info("something went wrong here {}",ex.getLocalizedMessage());
            return new RegisterResponse(Status.BAD_REQUEST, ex.getLocalizedMessage());
        } catch (OtpException | MessagingException | UnsupportedEncodingException e) {
            log.info(e.getLocalizedMessage());
            return new RegisterResponse(Status.INTERNAL_ERROR, e.getLocalizedMessage());
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try{
            validateLoginRequest(request);
            AppUser user = userService.findUserByEMail(request.getEmail());
//            checkIfPasswordMatchesUserPassword(request.getPassword(),user.getPassword());
//            checkIfUserIsEnabled(user.isEnabled());
            log.info("Here");
//            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
            Authentication authentication =customAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            log.info("Here something happened {}",authentication.isAuthenticated());
            log.info("Authentication {}",authentication);
            log.info("Context {}",SecurityContextHolder.getContext().getAuthentication());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Context {}",SecurityContextHolder.getContext().getAuthentication());
            log.info("Got here");
            String jwt = jwtTokenProvider.generateToken(authentication);
            log.info("Jwt {}",jwt);
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);
            log.info("Refresh Token {}", refreshToken);
            return new LoginResponse(Status.SUCCESS,
                    "Login successful", user.getUserId(),
                    jwt,
                    refreshToken.getToken(),
                    Instant.now());
        }catch (InvalidUserInputException|UserNotFoundException ex){
            log.info("here ni ohh");
            return new LoginResponse(Status.BAD_REQUEST, ex.getLocalizedMessage());
        }
    }

    private void validateRegisterRequest(RegisterRequest request) throws InvalidUserInputException {
        if (null == request.getEmail() ||  request.getEmail().isBlank() || request.getEmail().isEmpty()){
            throw new InvalidUserInputException("Email cannot be left empty or blank");
        }
        if (null == request.getPassword() ||  request.getPassword().isBlank() || request.getPassword().isEmpty()){
            throw new InvalidUserInputException("Password cannot be left empty or blank");
        }

        if (null == request.getUsername() ||  request.getUsername().isBlank() || request.getUsername().isEmpty()){
            throw new InvalidUserInputException("Username cannot be left empty or blank");
        }

        if (null == request.getFirstName() ||  request.getFirstName().isBlank() || request.getFirstName().isEmpty()){
            throw new InvalidUserInputException("Firstname cannot be left empty or blank");
        }
        if (null == request.getLastName() ||  request.getLastName().isBlank() || request.getLastName().isEmpty()){
            throw new InvalidUserInputException("Lastname cannot be left empty or blank");
        }

        if (null == request.getPhoneNumber() ||  request.getPhoneNumber().isBlank() || request.getPhoneNumber().isEmpty()){
            throw new InvalidUserInputException("Phone Number cannot be left empty or blank");
        }

        if (!request.getPhoneNumber().matches(Constant.PHONE_NUMBER_PATTERN)){
            throw new InvalidUserInputException("Phone number does not match the phone number pattern");
        }
    }
    private void checkIfUserWithEmailOrUsernameExist(String email, String username) throws UserAlreadyExistException {
        try {
            AppUser appUser = userService.findUserByEmailOrUsername(email,username);
            if (null != appUser){
                throw new UserAlreadyExistException("AppUser with email or username already exists");
            }
        } catch (UserNotFoundException ignored) {

        }
    }

    private void validateLoginRequest(LoginRequest request) throws InvalidUserInputException {
        if (null == request.getEmail() || request.getEmail().isBlank() || request.getEmail().isEmpty()){
            throw new InvalidUserInputException("Email is required!");
        }
        if (null == request.getPassword() || request.getPassword().isEmpty() || request.getPassword().isBlank()){
            throw new InvalidUserInputException("Password is required!");
        }
    }
    //Change the response to 401
    private void checkIfUserIsEnabled(boolean isEnabled) throws UserNorEnabledException {
        if (!isEnabled){
            throw new UserNorEnabledException("User not enabled!");
        }
    }
    //Change the response to 401
    private void checkIfPasswordMatchesUserPassword(String password, String userPassword) throws IncorrectPasswordExeption {
        if (!passwordEncoder.matches(password,userPassword)){
            throw new IncorrectPasswordExeption("Password is incorrect");
        }
    }
}
