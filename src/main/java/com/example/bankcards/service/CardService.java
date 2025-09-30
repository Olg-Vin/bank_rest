package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Autowired
    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public Card createCard(Long ownerId, String cardNumber, LocalDate validityPeriod) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Card card = new Card();
        card.setOwner(owner);
        card.setCardNumber(cardNumber);
        card.setValidityPeriod(validityPeriod);
        return cardRepository.save(card);
    }

    public List<Card> getCardsByOwner(Long ownerId) {
        return cardRepository.findAllByOwnerId(ownerId);
    }
}

