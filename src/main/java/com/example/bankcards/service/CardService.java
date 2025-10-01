package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.CardNotFoundException;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberEncryptionService;
import com.example.bankcards.util.MaskCardNumber;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberEncryptionService cardNumberEncryptionService;
    private final ModelMapper modelMapper;
    private final MaskCardNumber maskCardNumber;

    @Autowired
    public CardService(CardRepository cardRepository,
                       UserRepository userRepository,
                       CardNumberEncryptionService cardNumberEncryptionService,
                       ModelMapper modelMapper, MaskCardNumber maskCardNumber) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardNumberEncryptionService = cardNumberEncryptionService;
        this.modelMapper = modelMapper;
        this.maskCardNumber = maskCardNumber;
    }

    public CardDto createCard(Long ownerId, String cardNumber, LocalDate validityPeriod) throws Exception {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Card card = new Card();
        card.setOwner(owner);
        card.setCardNumber(cardNumberEncryptionService.encrypt(cardNumber));
        card.setValidityPeriod(validityPeriod);
        return modelMapper.map(cardRepository.save(card), CardDto.class);
    }

    public List<CardDto> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(card -> {
                    CardDto cardDto = modelMapper.map(card, CardDto.class);
                    cardDto.setCardNumber(maskCardNumber.maskNumber(card.getLast4()));
                    return cardDto;
                }).toList();
    }

    public List<CardDto> getCardByNumber(String number) {
        return cardRepository.findByCardNumber(number)
                .stream()
                .map(card -> {
                    CardDto cardDto = modelMapper.map(card, CardDto.class);
                    cardDto.setCardNumber(maskCardNumber.maskNumber(card.getLast4()));
                    return cardDto;
                }).toList();
    }

    public List<CardDto> getCardByLast4(String last4) {
        return cardRepository.findByLast4(last4)
                .stream()
                .map(card -> {
                    CardDto cardDto = modelMapper.map(card, CardDto.class);
                    cardDto.setCardNumber(maskCardNumber.maskNumber(card.getLast4()));
                    return cardDto;
                }).toList();
    }

    public List<CardDto> getCardsByOwner(Long ownerId) {
        return cardRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(card -> {
                    CardDto cardDto = modelMapper.map(card, CardDto.class);
                    cardDto.setCardNumber(maskCardNumber.maskNumber(card.getLast4()));
                    return cardDto;
                }).toList();
    }

    public void updateCardStatus(Long cardId, String status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        card.setStatus(status);
        cardRepository.save(card);
    }

    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        cardRepository.delete(card);
    }

    public BigDecimal getBalance(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        return card.getBalance();
    }

    public void blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        card.setStatus("BLOCKED");
        cardRepository.save(card);
    }
}

