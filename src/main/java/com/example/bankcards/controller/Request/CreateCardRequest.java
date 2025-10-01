package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Запрос на создание карты")
public record CreateCardRequest(
        @Schema(description = "ID владельца", example = "1")
        Long ownerId,
        @Schema(description = "Номер карты", example = "1234 5678 9012 3456")
        String cardNumber,
        @Schema(description = "Срок действия", example = "2026-12-31")
        LocalDate validityPeriod
) {
}
