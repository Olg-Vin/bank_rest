package com.example.bankcards.exception.exceptions;

import com.example.bankcards.exception.BaseException;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException(String message) {
        super("Неверные данные", message);
    }
}
