package com.example.bankcards.controller;

import com.example.bankcards.controller.Request.CreateUserRequest;
import com.example.bankcards.controller.Request.UpdateUserRequest;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;
    private CardDto cardDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userDto = new UserDto();
        userDto.setUsername("john");

        cardDto = new CardDto();
        cardDto.setCardNumber("1234567890123456");
    }

    @Test
    void createUser_success() throws Exception {
        CreateUserRequest request = new CreateUserRequest("john", "password123");
        when(userService.createUser("john", "password123")).thenReturn(userDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService).createUser("john", "password123");
    }

    @Test
    void getAllUsers_success() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john"));

        verify(userService).getAllUsers();
    }

    @Test
    void getUserInfo_success() throws Exception {
        when(userService.getUser(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService).getUser(1L);
    }

    @Test
    void getUserCards_success() throws Exception {
        when(cardService.getCardsByOwner(1L)).thenReturn(List.of(cardDto));

        mockMvc.perform(get("/api/users/1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1234567890123456"));

        verify(cardService).getCardsByOwner(1L);
    }

    @Test
    void getCardByLast4_success() throws Exception {
        when(cardService.getCardByLast4("3456")).thenReturn(List.of(cardDto));

        mockMvc.perform(get("/api/users/card/last4/3456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1234567890123456"));

        verify(cardService).getCardByLast4("3456");
    }

    @Test
    void getCardBalance_success() throws Exception {
        when(cardService.getBalance(1L)).thenReturn(BigDecimal.valueOf(1000));

        mockMvc.perform(get("/api/users/card/1/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000"));

        verify(cardService).getBalance(1L);
    }

    @Test
    void updateUser_success() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("john2", "pass2");
        when(userService.updateUser(1L, "john2", "pass2")).thenReturn(userDto);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService).updateUser(1L, "john2", "pass2");
    }

    @Test
    void requestBlockCard_success() throws Exception {
        mockMvc.perform(post("/api/users/card/1/block"))
                .andExpect(status().isNoContent());

        verify(cardService).blockCard(1L);
    }

    @Test
    void deleteUser_success() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }
}
