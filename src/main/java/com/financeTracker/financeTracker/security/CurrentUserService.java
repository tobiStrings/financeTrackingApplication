package com.financeTracker.financeTracker.security;


import com.financeTracker.financeTracker.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrentUserService {
    public static String getCurrentUserUsername(){
        String username =  (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("username {}",username);
        return username;
    }
}
