package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на изменение статуса транзакции")
public record UpdateTransactionStatusRequest(
        @Schema(description = "Новый статус транзакции", example = "SUCCESS")
        String status
) {
}
