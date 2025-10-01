package com.example.bankcards.controller;

import com.example.bankcards.controller.Request.TransactionDateRangeRequest;
import com.example.bankcards.controller.Request.TransactionRequest;
import com.example.bankcards.controller.Request.UpdateTransactionStatusRequest;
import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.service.TransactionService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        transactionDto = new TransactionDto();
        transactionDto.setAmount(String.valueOf(BigDecimal.valueOf(500)));
    }

    @Test
    void makeTransfer_success() throws Exception {
        TransactionRequest request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(500));

        when(transactionService.makeTransfer(1L, 2L, BigDecimal.valueOf(500))).thenReturn(transactionDto);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500));

        verify(transactionService).makeTransfer(1L, 2L, BigDecimal.valueOf(500));
    }

    @Test
    void getTransactionsByCard_success() throws Exception {
        when(transactionService.getTransactionsByCard(1L)).thenReturn(List.of(transactionDto));

        mockMvc.perform(get("/api/transactions/card/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(500));

        verify(transactionService).getTransactionsByCard(1L);
    }

    @Test
    void getTransactionsByDate_success() throws Exception {
        TransactionDateRangeRequest request = new TransactionDateRangeRequest(LocalDate.now().minusDays(1).atStartOfDay(), LocalDate.now().atStartOfDay());
        when(transactionService.getTransactionsByDateRange(request.fromDate(), request.toDate()))
                .thenReturn(List.of(transactionDto));

        mockMvc.perform(post("/api/transactions/date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(500));

        verify(transactionService).getTransactionsByDateRange(request.fromDate(), request.toDate());
    }

    @Test
    void updateTransactionStatus_success() throws Exception {
        UpdateTransactionStatusRequest request = new UpdateTransactionStatusRequest("COMPLETED");

        mockMvc.perform(patch("/api/transactions/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(transactionService).updateTransactionStatus(1L, "COMPLETED");
    }
}


