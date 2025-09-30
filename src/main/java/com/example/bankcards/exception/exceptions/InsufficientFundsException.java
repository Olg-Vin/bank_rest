package com.example.bankcards.exception.exceptions;

import com.example.bankcards.exception.BaseException;

public class InsufficientFundsException extends BaseException {
    public InsufficientFundsException(String message) {
        super("Недостаточно средств", message);
    }
}
