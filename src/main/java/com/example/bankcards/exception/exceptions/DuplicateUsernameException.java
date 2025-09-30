package com.example.bankcards.exception.exceptions;

import com.example.bankcards.exception.BaseException;

public class DuplicateUsernameException extends BaseException {
    public DuplicateUsernameException(String message) {
        super("Такое имя пользователя уже существует", message);
    }
}
