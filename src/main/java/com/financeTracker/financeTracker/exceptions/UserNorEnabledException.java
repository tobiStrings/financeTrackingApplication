package com.financeTracker.financeTracker.exceptions;

public class UserNorEnabledException extends FinanceTrackerException{
    public UserNorEnabledException(String message) {
        super(message);
    }

    public UserNorEnabledException() {
    }
}
