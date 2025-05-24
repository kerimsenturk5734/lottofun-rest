package com.lottofun.lottofunrest.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("Insufficient balance to purchase the ticket.");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
