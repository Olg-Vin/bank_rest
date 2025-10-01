package com.example.bankcards.controller;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "Операции с транзакциями")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Создать транзакцию", description = "Перевод денег между картами")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<TransactionDto> makeTransfer(
            @RequestParam Long fromCardId,
            @RequestParam Long toCardId,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(transactionService.makeTransfer(fromCardId, toCardId, amount));
    }

    @Operation(summary = "Получить транзакции по карте")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCard(cardId));
    }

    @Operation(summary = "Получить транзакции за период", description = "Доступно только ADMIN")
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date")
    public ResponseEntity<List<TransactionDto>> getTransactionsByDate(
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(fromDate, toDate));
    }

    @Operation(summary = "Изменить статус транзакции", description = "Доступно только ADMIN")
//    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{transactionId}/status")
    public ResponseEntity<Void> updateTransactionStatus(@PathVariable Long transactionId,
                                                        @RequestParam String status) {
        transactionService.updateTransactionStatus(transactionId, status);
        return ResponseEntity.noContent().build();
    }
}

