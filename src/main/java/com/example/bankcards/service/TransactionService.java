package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.exceptions.CardNotFoundException;
import com.example.bankcards.exception.exceptions.InsufficientFundsException;
import com.example.bankcards.exception.exceptions.TransactionNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.util.mapper.TransactionMapper;
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
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              CardRepository cardRepository,
                              TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionDto makeTransfer(Long fromCardId, Long toCardId, BigDecimal amount) {
        log.info("makeTransfer from {} to {} amount {}", fromCardId, toCardId, amount);

        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new CardNotFoundException("From card not found"));

        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new CardNotFoundException("To card not found"));

        // Проверка баланса отправителя
        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds on card");
        }

        // Обновление балансов
        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        // Создание транзакции через маппер
        TransactionDto dto = new TransactionDto();
        dto.setAmount(amount.toPlainString());
        dto.setStatus("SUCCESS");

        Transaction transaction = TransactionMapper.toEntity(dto, fromCard, toCard);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    public List<TransactionDto> getTransactionsByCard(Long cardId) {
        return transactionRepository.findAllByCardId(cardId)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    public List<TransactionDto> getTransactionsByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionRepository.findAllByDateRange(fromDate, toDate)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    public void updateTransactionStatus(Long transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        transaction.setStatus(status);
        transactionRepository.save(transaction);
    }
}