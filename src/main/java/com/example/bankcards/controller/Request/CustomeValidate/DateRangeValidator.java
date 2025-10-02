package com.example.bankcards.controller.Request.CustomeValidate;

import com.example.bankcards.controller.Request.TransactionDateRangeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, TransactionDateRangeRequest> {

    @Override
    public boolean isValid(TransactionDateRangeRequest value, ConstraintValidatorContext context) {
        if (value == null) return true; // null проверяется другими аннотациями
        LocalDateTime from = value.fromDate();
        LocalDateTime to = value.toDate();
        return from != null && to != null && !from.isAfter(to);
    }
}

