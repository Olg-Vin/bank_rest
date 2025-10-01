package com.example.bankcards.controller.Request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Запрос на получение транзакций за период")
public record TransactionDateRangeRequest(
        @Schema(description = "Дата начала", example = "2023-01-01T00:00:00")
        LocalDateTime fromDate,
        @Schema(description = "Дата окончания", example = "2023-01-31T23:59:59")
        LocalDateTime toDate
) {
}
