package com.financeTracker.financeTracker.security;

import com.financeTracker.financeTracker.exceptions.IncorrectPasswordExeption;
import com.financeTracker.financeTracker.exceptions.UserNorEnabledException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userPrincipal = customUserDetailsService.loadUserByUsername(authentication.getName());
        try {
            checkIfUserIsEnabled(userPrincipal.isEnabled());
            checkIfPasswordMatchesUserPassword(authentication.getCredentials().toString(),userPrincipal.getPassword());

        } catch (UserNorEnabledException | IncorrectPasswordExeption e) {
                throw new RuntimeException(e);
        }

        return new UsernamePasswordAuthenticationToken(userPrincipal,userPrincipal.getPassword(),userPrincipal.getAuthorities());
    }

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
