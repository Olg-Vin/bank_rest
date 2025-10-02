package com.example.bankcards.controller.Request.CustomeValidate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TransactionValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTransaction {
    String message() default "ID карты отправителя и получателя не могут совпадать";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

