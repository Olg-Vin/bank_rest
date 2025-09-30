package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.exceptions.CardNotFoundException;
import com.example.bankcards.exception.exceptions.InsufficientFundsException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    public Transaction makeTransfer(Long fromCardId, Long toCardId, BigDecimal amount) {
        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new CardNotFoundException("From card not found"));

        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new CardNotFoundException("To card not found"));

        // Проверка баланса отправителя
        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds on card");
        }

        // Блокировка средств
        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        // Создание транзакции
        Transaction transaction = new Transaction();
        transaction.setFromCard(fromCard);
        transaction.setToCard(toCard);
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByCard(Long cardId) {
        return transactionRepository.findAllByCardId(cardId);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionRepository.findAllByDateRange(fromDate, toDate);
    }
}