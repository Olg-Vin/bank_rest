package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardNumberEncryptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Card Controller", description = "Операции с банковскими картами")
@Log4j2
public class CardController {

    private final CardService cardService;
    private final CardNumberEncryptionService cardNumberEncryptionService;

    @Autowired
    public CardController(CardService cardService, CardNumberEncryptionService cardNumberEncryptionService) {
        this.cardService = cardService;
        this.cardNumberEncryptionService = cardNumberEncryptionService;
    }

    @Operation(summary = "Создать карту", description = "Создание новой карты (только для ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Карта создана"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CardDto> createCard(
            @Parameter(description = "ID владельца") @RequestParam Long ownerId,
            @Parameter(description = "Номер карты") @RequestParam String cardNumber,
            @Parameter(description = "Срок действия") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod
    ) throws Exception {
        log.info("Когда тут будет вызов?..");
        return ResponseEntity.ok(cardService.createCard(ownerId, cardNumberEncryptionService.encrypt(cardNumber), validityPeriod));
    }

    @Operation(summary = "Получить все карты", description = "Возвращает список всех карт (только для ADMIN)")
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CardDto>> getAllCards() {
        log.info("Снова здесь вызов");
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @Operation(summary = "Найти карту по номеру", description = "Возвращает карту по полному номеру")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/number/{number}")
    public ResponseEntity<List<CardDto>> getCardByNumber(@PathVariable String number) {
        return ResponseEntity.ok(cardService.getCardByNumber(number));
    }

    @Operation(summary = "Изменить статус карты", description = "ADMIN может изменить статус карты")
//    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{cardId}/status")
    public ResponseEntity<Void> updateCardStatus(@PathVariable Long cardId,
                                                 @RequestParam String status) {
        cardService.updateCardStatus(cardId, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить карту", description = "Удаление карты (ADMIN)")
//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}

