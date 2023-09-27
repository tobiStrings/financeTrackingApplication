package com.financeTracker.financeTracker.exceptions;

public class IncorrectPasswordExeption extends FinanceTrackerException{
    public IncorrectPasswordExeption() {
    }

    public IncorrectPasswordExeption(String message) {
        super(message);
    }
}
