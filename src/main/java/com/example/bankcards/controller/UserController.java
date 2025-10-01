package com.example.bankcards.controller;

import com.example.bankcards.controller.Request.CreateUserRequest;
import com.example.bankcards.controller.Request.UpdateUserRequest;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Операции с пользователями и их картами")
public class UserController {

    private final UserService userService;
    private final CardService cardService;

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request.username(), request.password()));
    }

    @Operation(summary = "Получить всех пользователей", description = "ADMIN может видеть всех")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Информация о пользователе")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @Operation(summary = "Карты пользователя")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{userId}/cards")
    public ResponseEntity<List<CardDto>> getUserCards(@PathVariable Long userId) {
        return ResponseEntity.ok(cardService.getCardsByOwner(userId));
    }

    @Operation(summary = "Найти карту по последним 4 цифрам")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/card/last4/{last4}")
    public ResponseEntity<List<CardDto>> getCardByLast4(@PathVariable String last4) {
        return ResponseEntity.ok(cardService.getCardByLast4(last4));
    }

    @Operation(summary = "Баланс карты")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/card/{cardId}/balance")
    public ResponseEntity<BigDecimal> getCardBalance(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.getBalance(cardId));
    }

    @Operation(summary = "Обновить данные пользователя")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
                                              @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request.username(), request.password()));
    }

    @Operation(summary = "Запрос на блокировку карты")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/card/{cardId}/block")
    public ResponseEntity<Void> requestBlockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить пользователя", description = "Доступно только ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}


