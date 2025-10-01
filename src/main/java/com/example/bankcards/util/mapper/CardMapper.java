package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.CardNumberEncryptionService;
import com.example.bankcards.util.MaskCardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CardMapper {
    private final CardNumberEncryptionService encryptionService;
    private final MaskCardNumber maskCardNumber;

    @Autowired
    public CardMapper(CardNumberEncryptionService encryptionService, MaskCardNumber maskCardNumber) {
        this.encryptionService = encryptionService;
        this.maskCardNumber = maskCardNumber;
    }

    public CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }

        CardDto cardDto = new CardDto();
        cardDto.setStatus(card.getStatus());
        cardDto.setValidityPeriod(String.valueOf(card.getValidityPeriod()));
        cardDto.setOwner(String.valueOf(card.getOwner().getId()));
        cardDto.setCardNumber(maskCardNumber.maskNumber(card.getLast4())); // **** **** **** 1234
        return cardDto;
    }

    public Card toEntity(CardDto dto, User owner) throws Exception {
        if (dto == null) {
            return null;
        }

        Card card = new Card();
        card.setOwner(owner);

        if (dto.getCardNumber() != null) {
            String encrypted = encryptionService.encrypt(dto.getCardNumber());
            card.setCardNumber(encrypted);
            if (dto.getCardNumber().length() >= 4) {
                card.setLast4(dto.getCardNumber().substring(dto.getCardNumber().length() - 4));
            }
        }

        if (dto.getValidityPeriod() != null) {
            card.setValidityPeriod(LocalDate.parse(dto.getValidityPeriod()));
        }

        card.setStatus(dto.getStatus() != null ? dto.getStatus() : "ACTIVE");
        return card;
    }
}

