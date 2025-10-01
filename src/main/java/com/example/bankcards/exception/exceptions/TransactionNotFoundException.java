package com.example.bankcards.exception.exceptions;

import com.example.bankcards.exception.BaseException;

public class TransactionNotFoundException extends BaseException {
    public TransactionNotFoundException(String message) {
        super("Транзакция не обнаружена", message);
    }
}
