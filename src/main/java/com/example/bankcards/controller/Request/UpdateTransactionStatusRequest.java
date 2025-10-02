package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Запрос на изменение статуса транзакции")
public record UpdateTransactionStatusRequest(

        @NotBlank(message = "Статус транзакции обязателен")
        @Pattern(regexp = "PENDING|SUCCESS|FAILED",
                message = "Статус транзакции должен быть 'PENDING', 'SUCCESS' или 'FAILED'")
        @Schema(description = "Новый статус транзакции", example = "SUCCESS")
        String status
) {
}

