package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.exceptions.InsufficientFundsException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.util.mapper.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    private Card fromCard;
    private Card toCard;

    @BeforeEach
    void setUp() {
        fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setBalance(BigDecimal.valueOf(200));

        toCard = new Card();
        toCard.setId(2L);
        toCard.setBalance(BigDecimal.valueOf(100));
    }

    @Test
    void makeTransfer_success() {
        BigDecimal amount = BigDecimal.valueOf(50);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        when(transactionMapper.toDto(any(Transaction.class))).thenAnswer(i -> new TransactionDto());

        TransactionDto result = transactionService.makeTransfer(1L, 2L, amount);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150), fromCard.getBalance());
        assertEquals(BigDecimal.valueOf(150), toCard.getBalance());
    }

    @Test
    void makeTransfer_insufficientFunds() {
        BigDecimal amount = BigDecimal.valueOf(500);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        assertThrows(InsufficientFundsException.class, () ->
                transactionService.makeTransfer(1L, 2L, amount)
        );
    }
}

