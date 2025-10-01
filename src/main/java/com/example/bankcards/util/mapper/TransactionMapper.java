package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDto dto = new TransactionDto();
        dto.setFromCard(String.valueOf(transaction.getFromCard().getId()));
        dto.setToCard(String.valueOf(transaction.getToCard().getId()));
        dto.setAmount(transaction.getAmount().toPlainString()); // BigDecimal -> String
        dto.setStatus(transaction.getStatus());
        return dto;
    }

    public static Transaction toEntity(TransactionDto dto, Card fromCard, Card toCard) {
        if (dto == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setFromCard(fromCard);
        transaction.setToCard(toCard);

        if (dto.getAmount() != null) {
            transaction.setAmount(new BigDecimal(dto.getAmount()));
        }

        transaction.setStatus(dto.getStatus() != null ? dto.getStatus() : "CREATE");
        return transaction;
    }
}

