package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Запрос на обновление данных пользователя")
public record UpdateUserRequest(
        @Schema(description = "Имя пользователя", example = "john_updated")
        String username,
        @Schema(description = "Пароль", example = "newpassword123")
        String password
) {}
