package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание пользователя")
public record CreateUserRequest(
        @Schema(description = "Имя пользователя", example = "john_doe")
        String username,
        @Schema(description = "Пароль", example = "password123")
        String password
) {
}
