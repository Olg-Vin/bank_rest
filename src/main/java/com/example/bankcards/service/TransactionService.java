package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.exceptions.CardNotFoundException;
import com.example.bankcards.exception.exceptions.InsufficientFundsException;
import com.example.bankcards.exception.exceptions.TransactionNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Log4j2
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
    }

    public TransactionDto makeTransfer(Long fromCardId, Long toCardId, BigDecimal amount) {
        log.info("makeTransfer");
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

        return modelMapper.map(transactionRepository.save(transaction), TransactionDto.class);
    }

    public List<TransactionDto> getTransactionsByCard(Long cardId) {
        return transactionRepository.findAllByCardId(cardId)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .toList();
    }

    public List<TransactionDto> getTransactionsByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionRepository.findAllByDateRange(fromDate, toDate)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .toList();
    }

    public void updateTransactionStatus(Long transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        transaction.setStatus(status);
        transactionRepository.save(transaction);
    }
}