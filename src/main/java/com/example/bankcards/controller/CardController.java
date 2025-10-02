package com.example.bankcards.controller;

import com.example.bankcards.controller.Request.CreateCardRequest;
import com.example.bankcards.controller.Request.UpdateCardStatusRequest;
import com.example.bankcards.controller.Response.PageResponse;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardNumberEncryptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CreateCardRequest request) throws Exception {
        return ResponseEntity.ok(
                cardService.createCard(
                        request.ownerId(),
                        request.cardNumber(),
                        request.validityPeriod()
                )
        );
    }

    @Operation(summary = "Получить все карты", description = "Возвращает список всех карт (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponse<CardDto>> getAllCards(Pageable pageable) {
        Page<CardDto> cards = cardService.getAllCards(pageable);
        return ResponseEntity.ok(new PageResponse<>(
                cards.getContent(),
                cards.getNumber(),
                cards.getSize(),
                cards.getTotalElements(),
                cards.getTotalPages()
        ));
    }

    @Operation(summary = "Найти карту по номеру", description = "Возвращает карту по полному номеру")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/number/{number}")
    public ResponseEntity<List<CardDto>> getCardByNumber(@PathVariable String number) {
        return ResponseEntity.ok(cardService.getCardByNumber(number));
    }

    @Operation(summary = "Изменить статус карты", description = "ADMIN может изменить статус карты")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{cardId}/status")
    public ResponseEntity<Void> updateCardStatus(@PathVariable Long cardId,
                                                 @Valid @RequestBody UpdateCardStatusRequest request) {
        cardService.updateCardStatus(cardId, request.status());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить карту", description = "Удаление карты (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}


