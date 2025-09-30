package com.example.bankcards.exception.exceptions;

import com.example.bankcards.exception.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super("Пользователь не найден", message);
    }
}
