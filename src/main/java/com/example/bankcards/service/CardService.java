package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.CardNotFoundException;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.mapper.CardMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    @Autowired
    public CardService(CardRepository cardRepository,
                       UserRepository userRepository,
                       CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardMapper = cardMapper;
    }

    public CardDto createCard(Long ownerId, String cardNumber, LocalDate validityPeriod) throws Exception {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CardDto cardDto = new CardDto();
        cardDto.setCardNumber(cardNumber);
        cardDto.setValidityPeriod(String.valueOf(validityPeriod));

        Card card = cardMapper.toEntity(cardDto, owner);
        log.info("Creating card for user {} with last4 {}", ownerId, card.getLast4());

        Card savedCard = cardRepository.save(card);
        return cardMapper.toDto(savedCard);
    }

    public List<CardDto> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(cardMapper::toDto)
                .toList();
    }

    public Page<CardDto> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(cardMapper::toDto);
    }


    public List<CardDto> getCardByNumber(String number) {
        return cardRepository.findByCardNumber(number)
                .stream()
                .map(cardMapper::toDto)
                .toList();
    }

    public List<CardDto> getCardByLast4(String last4) {
        return cardRepository.findByLast4(last4)
                .stream()
                .map(cardMapper::toDto)
                .toList();
    }

    public List<CardDto> getCardsByOwner(Long ownerId) {
        return cardRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(cardMapper::toDto)
                .toList();
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