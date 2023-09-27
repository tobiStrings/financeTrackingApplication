package com.financeTracker.financeTracker.security;

import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.data.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findUSerByUsername(username).orElseThrow(()->new UsernameNotFoundException("User with email "+username+" not found"));

        return UserPrincipal.create(appUser);
    }
}
