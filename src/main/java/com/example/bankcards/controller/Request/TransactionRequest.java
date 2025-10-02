package com.example.bankcards.controller.Request;

import com.example.bankcards.controller.Request.CustomeValidate.ValidTransaction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Запрос на перевод средств")
@ValidTransaction
public record TransactionRequest(

        @NotNull(message = "ID карты-отправителя обязателен")
        @Schema(description = "ID карты-отправителя", example = "1")
        Long fromCardId,

        @NotNull(message = "ID карты-получателя обязателен")
        @Schema(description = "ID карты-получателя", example = "2")
        Long toCardId,

        @NotNull(message = "Сумма перевода обязательна")
        @Positive(message = "Сумма перевода должна быть больше 0")
        @Schema(description = "Сумма перевода", example = "1000.50")
        BigDecimal amount
) {
}

