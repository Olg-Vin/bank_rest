package com.example.bankcards.controller.Request;

import com.example.bankcards.controller.Request.CustomeValidate.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на получение транзакций за период")
@ValidDateRange
public record TransactionDateRangeRequest(

        @NotNull(message = "Дата начала обязателен")
        @Schema(description = "Дата начала", example = "2023-01-01T00:00:00")
        LocalDateTime fromDate,

        @NotNull(message = "Дата окончания обязателен")
        @Schema(description = "Дата окончания", example = "2023-01-31T23:59:59")
        LocalDateTime toDate
) {}

