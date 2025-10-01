package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Запрос на перевод средств")
public record TransactionRequest(
        @Schema(description = "ID карты-отправителя", example = "1")
        Long fromCardId,
        @Schema(description = "ID карты-получателя", example = "2")
        Long toCardId,
        @Schema(description = "Сумма перевода", example = "1000.50")
        BigDecimal amount
) {
}
