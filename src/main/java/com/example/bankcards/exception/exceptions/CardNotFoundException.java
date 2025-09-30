package com.example.bankcards.exception.exceptions;

import com.example.bankcards.exception.BaseException;

public class CardNotFoundException extends BaseException {
    public CardNotFoundException(String message) {
        super("Карта не обнаружена", message);
    }
}
