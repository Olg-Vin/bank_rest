package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Schema(description = "Запрос на создание карты")
public record CreateCardRequest(
        @NotNull(message = "ID владельца обязателен")
        @Schema(description = "ID владельца", example = "1")
        Long ownerId,

        @NotBlank(message = "Номер карты обязателен")
        @Pattern(regexp = "\\d{16}",
                message = "Номер карты должен быть в формате '1234 5678 9012 3456'")
        @Schema(description = "Номер карты", example = "1234 5678 9012 3456")
        String cardNumber,

        @NotNull(message = "Срок действия обязателен")
        @Future(message = "Срок действия карты должен быть в будущем")
        @Schema(description = "Срок действия", example = "2026-12-31")
        LocalDate validityPeriod
) {
}

