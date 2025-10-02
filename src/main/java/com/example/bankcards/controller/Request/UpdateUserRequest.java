package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на обновление данных пользователя")
public record UpdateUserRequest(

        @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
        @Schema(description = "Имя пользователя", example = "john_updated")
        String username,

        @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
        @Schema(description = "Пароль", example = "newpassword123")
        String password
) {
}

