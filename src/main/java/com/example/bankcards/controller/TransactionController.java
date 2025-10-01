package com.example.bankcards.controller;

import com.example.bankcards.controller.Request.TransactionDateRangeRequest;
import com.example.bankcards.controller.Request.TransactionRequest;
import com.example.bankcards.controller.Request.UpdateTransactionStatusRequest;
import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "Операции с транзакциями")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Создать транзакцию", description = "Перевод денег между картами")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<TransactionDto> makeTransfer(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(
                transactionService.makeTransfer(request.fromCardId(), request.toCardId(), request.amount())
        );
    }

    @Operation(summary = "Получить транзакции по карте")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCard(cardId));
    }

    @Operation(summary = "Получить транзакции за период", description = "Доступно только ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/date")
    public ResponseEntity<List<TransactionDto>> getTransactionsByDate(@RequestBody TransactionDateRangeRequest request) {
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(request.fromDate(), request.toDate()));
    }

    @Operation(summary = "Изменить статус транзакции", description = "Доступно только ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{transactionId}/status")
    public ResponseEntity<Void> updateTransactionStatus(@PathVariable Long transactionId,
                                                        @RequestBody UpdateTransactionStatusRequest request) {
        transactionService.updateTransactionStatus(transactionId, request.status());
        return ResponseEntity.noContent().build();
    }
}


