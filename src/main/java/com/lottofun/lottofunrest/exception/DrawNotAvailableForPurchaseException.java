package com.lottofun.lottofunrest.exception;

public class DrawNotAvailableForPurchaseException extends RuntimeException {
    public DrawNotAvailableForPurchaseException() {
        super("Draw is not open for ticket purchase.");
    }

    public DrawNotAvailableForPurchaseException(String message) {
        super(message);
    }
}
