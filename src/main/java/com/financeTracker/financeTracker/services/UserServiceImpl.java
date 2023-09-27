package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.data.repositories.UserRepository;
import com.financeTracker.financeTracker.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    @Override
    public AppUser saveUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    @Override
    public AppUser findUserByEmailOrUsername(String email, String username) throws UserNotFoundException {
        return userRepository.findUserByEmailOrUsername(email,username).orElseThrow(()-> new UserNotFoundException("User not found"));
    }

    @Override
    public AppUser findUserByEMail(String email) throws UserNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException("User with email not found!"));
    }

    @Override
    public AppUser findUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findUSerByUsername(username).orElseThrow(()-> new UserNotFoundException("User with username not found!"));
    }
}
