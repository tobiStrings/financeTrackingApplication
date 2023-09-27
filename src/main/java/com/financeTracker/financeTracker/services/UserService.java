package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.model.AppUser;
import com.financeTracker.financeTracker.exceptions.UserNotFoundException;

public interface UserService {
    AppUser saveUser(AppUser appUser);
    AppUser findUserByEmailOrUsername(String email, String username) throws UserNotFoundException;
    AppUser findUserByEMail(String email) throws UserNotFoundException;
    AppUser findUserByUsername(String username) throws UserNotFoundException;
}
