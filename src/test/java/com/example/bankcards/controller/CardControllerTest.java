package com.example.bankcards.controller;

import com.example.bankcards.controller.Request.CreateCardRequest;
import com.example.bankcards.controller.Request.UpdateCardStatusRequest;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardNumberEncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @Mock
    private CardNumberEncryptionService cardNumberEncryptionService;

    @InjectMocks
    private CardController cardController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private CardDto cardDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        cardDto = new CardDto();
        cardDto.setCardNumber("1234567890123456");
    }

    @Test
    void createCard_success() throws Exception {

        CreateCardRequest request = new CreateCardRequest(1L, "1234567890123456", LocalDate.now());

        when(cardService.createCard(1L, "1234567890123456", LocalDate.now())).thenReturn(cardDto);

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("1234567890123456"));

        verify(cardService).createCard(1L, "1234567890123456", LocalDate.now());
    }

    @Test
    void getAllCards_success() throws Exception {
        when(cardService.getAllCards()).thenReturn(List.of(cardDto));

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1234567890123456"));

        verify(cardService).getAllCards();
    }

    @Test
    void getCardByNumber_success() throws Exception {
        when(cardService.getCardByNumber("1234567890123456")).thenReturn(List.of(cardDto));

        mockMvc.perform(get("/api/cards/number/1234567890123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1234567890123456"));

        verify(cardService).getCardByNumber("1234567890123456");
    }

    @Test
    void updateCardStatus_success() throws Exception {
        UpdateCardStatusRequest request = new UpdateCardStatusRequest("BLOCKED");

        mockMvc.perform(patch("/api/cards/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(cardService).updateCardStatus(1L, "BLOCKED");
    }

    @Test
    void deleteCard_success() throws Exception {
        mockMvc.perform(delete("/api/cards/1"))
                .andExpect(status().isNoContent());

        verify(cardService).deleteCard(1L);
    }
}
