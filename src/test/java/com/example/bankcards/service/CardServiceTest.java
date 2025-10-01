package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.mapper.CardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    private User user;
    private Card card;
    private CardDto cardDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john");

        card = new Card();
        card.setId(1L);
        card.setCardNumber("1234567890123456");
        card.setLast4("3456");
        card.setOwner(user);
        card.setBalance(BigDecimal.valueOf(100));

        cardDto = new CardDto();
        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setValidityPeriod("2030-12-31");
    }

    @Test
    void createCard_success() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardMapper.toEntity(cardDto, user)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        CardDto result = cardService.createCard(1L, "1234567890123456", LocalDate.of(2030, 12, 31));

        assertNotNull(result);
        assertEquals("1234567890123456", result.getCardNumber());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void createCard_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () ->
                cardService.createCard(1L, "1234567890123456", LocalDate.of(2030, 12, 31))
        );
    }

    @Test
    void getBalance_success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        BigDecimal balance = cardService.getBalance(1L);
        assertEquals(BigDecimal.valueOf(100), balance);
    }

    @Test
    void blockCard_success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        cardService.blockCard(1L);
        assertEquals("BLOCKED", card.getStatus());
        verify(cardRepository).save(card);
    }
}

