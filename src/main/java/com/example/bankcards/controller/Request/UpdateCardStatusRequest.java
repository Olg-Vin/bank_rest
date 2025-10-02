package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Запрос на изменение статуса карты")
public record UpdateCardStatusRequest(

        @NotBlank(message = "Статус карты обязателен")
        @Pattern(regexp = "ACTIVE|BLOCKED",
                message = "Статус карты должен быть либо 'ACTIVE', либо 'BLOCKED'")
        @Schema(description = "Новый статус карты", example = "BLOCKED")
        String status
) {
}

