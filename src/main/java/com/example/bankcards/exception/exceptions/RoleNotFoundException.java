package com.example.bankcards.exception.exceptions;

import com.example.bankcards.exception.BaseException;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(String message) {
        super("Роль не обнаружена", message);
    }
}
