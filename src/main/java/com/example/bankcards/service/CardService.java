package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberEncryptionService cardNumberEncryptionService;

    @Autowired
    public CardService(CardRepository cardRepository,
                       UserRepository userRepository,
                       CardNumberEncryptionService cardNumberEncryptionService) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardNumberEncryptionService = cardNumberEncryptionService;
    }

    public Card createCard(Long ownerId, String cardNumber, LocalDate validityPeriod) throws Exception {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Card card = new Card();
        card.setOwner(owner);
        card.setCardNumber(cardNumberEncryptionService.encrypt(cardNumber));
        card.setValidityPeriod(validityPeriod);
        return cardRepository.save(card);
    }

    public List<Card> getCardsByOwner(Long ownerId) {
        return cardRepository.findAllByOwnerId(ownerId);
    }

    private String maskPan(String pan) {
        if (pan == null) return null;
        String digits = pan.replaceAll("\\D", "");
        int len = digits.length();
        if (len <= 4) return digits; // что возвращать — по бизнес-логике

        String last4 = digits.substring(len - 4);
        // Формируем "**** **** **** 1234" — с пробелами каждые 4 символа для читаемости.
        return "**** **** **** " + last4;
    }
}

