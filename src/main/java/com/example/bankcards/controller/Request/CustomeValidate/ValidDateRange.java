package com.example.bankcards.controller.Request.CustomeValidate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Дата начала должна быть раньше или равна дате окончания";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
