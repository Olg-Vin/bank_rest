package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на изменение статуса карты")
public record UpdateCardStatusRequest(
        @Schema(description = "Новый статус карты", example = "BLOCKED")
        String status
) {
}
