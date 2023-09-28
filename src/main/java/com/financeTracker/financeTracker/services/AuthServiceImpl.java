package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.*;
import com.financeTracker.financeTracker.data.model.RefreshToken;
import com.financeTracker.financeTracker.exceptions.*;
import com.financeTracker.financeTracker.security.CustomAuthenticationManager;
import com.financeTracker.financeTracker.security.JwtTokenProvider;
import com.financeTracker.financeTracker.security.UserPrincipal;
import com.financeTracker.financeTracker.utils.Constant;
import com.financeTracker.financeTracker.data.enums.Role;
import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
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
                    .enabled(true)
                    .createdDate(new Date())
                    .build();
            userService.saveUser(appUser);
            log.info("Here before returning response!");
            return new RegisterResponse(Status.CREATED,"User created Successfully");
        }catch (InvalidUserInputException| UserAlreadyExistException ex){
            log.info("something went wrong here {}",ex.getLocalizedMessage());
            return new RegisterResponse(Status.BAD_REQUEST, ex.getLocalizedMessage());
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try{
            validateLoginRequest(request);
            AppUser user = userService.findUserByUsername(request.getUsername());
            log.info("Nibi");
            Authentication authentication =customAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Ita");
            String jwt = jwtTokenProvider.generateToken(authentication);
            log.info("Jwt {}", jwt);
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);

            return new LoginResponse(Status.SUCCESS,
                    "Login successful", user.getId(),
                    jwt,
                    refreshToken.getToken(),
                    Instant.now());
        }catch (InvalidUserInputException ex){
            log.info("here ni ohh");
            return new LoginResponse(Status.BAD_REQUEST, ex.getLocalizedMessage());
        }catch (UserNotFoundException ex){
            return new LoginResponse(Status.NOT_FOUND,ex.getLocalizedMessage());
        }
    }

    @Override
    public ReGenerateTokenResponse regenerateToken(String refreshToken) {
        try{
            if (null == refreshToken || refreshToken.isEmpty() || refreshToken.isBlank()){
                throw new InvalidUserInputException("Refresh token is required");
            }
            if (refreshTokenService.checkRefreshTokenExpiration(refreshToken)){
                refreshTokenService.deleteRefreshToken(refreshToken);
                throw new RefreshTokenException("Refresh token has expired!");
            }
            RefreshToken token = refreshTokenService.findRefreshTokenByRefreshToken(refreshToken);
            UserDetails userDetails = UserPrincipal.create(token.getAppUser());
            String jwt = jwtTokenProvider.generateToken(userDetails);
            return new ReGenerateTokenResponse(Status.SUCCESS,"Token generated successfully",jwt,refreshToken);
        }catch (InvalidUserInputException ex){
            return new ReGenerateTokenResponse(Status.BAD_REQUEST,ex.getLocalizedMessage());
        } catch (RefreshTokenException e) {
            return new ReGenerateTokenResponse(Status.NOT_FOUND,e.getLocalizedMessage());
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
        if (null == request.getUsername() || request.getUsername().isBlank() || request.getUsername().isEmpty()){
            throw new InvalidUserInputException("Email is required!");
        }
        if (null == request.getPassword() || request.getPassword().isEmpty() || request.getPassword().isBlank()){
            throw new InvalidUserInputException("Password is required!");
        }
    }


}
