package com.financeTracker.financeTracker.security;

import com.financeTracker.financeTracker.data.model.User;
import com.financeTracker.financeTracker.data.repositories.UserRepository;
import com.financeTracker.financeTracker.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUSerByUsername(username).orElseThrow(()->new UsernameNotFoundException("User with username "+username+" not found"));
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return userPrincipal;
    }
}
