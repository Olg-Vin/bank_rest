package com.example.bankcards.controller.Request.CustomeValidate;

import com.example.bankcards.controller.Request.TransactionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TransactionValidator implements ConstraintValidator<ValidTransaction, TransactionRequest> {

    @Override
    public boolean isValid(TransactionRequest value, ConstraintValidatorContext context) {
        if (value == null) return true;
        Long from = value.fromCardId();
        Long to = value.toCardId();
        return from != null && to != null && !from.equals(to);
    }
}

