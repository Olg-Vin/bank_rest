package com.example.bankcards.controller;

import com.example.bankcards.security.jwt.JwtRequest;
import com.example.bankcards.security.jwt.JwtResponse;
import com.example.bankcards.security.jwt.RefreshJwtRequest;
import com.example.bankcards.service.AuthService;
import com.example.bankcards.service.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          UserService userService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registration(@RequestBody JwtRequest registerRequest) {
        try {
            // Проверяем, есть ли уже пользователь
            if (userService.existUserByName(registerRequest.getUsername())) {
                log.warn("Registration failed: username {} already exists", registerRequest.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Username already exists"));
            }

            // Создаем пользователя
            userService.createUser(registerRequest.getUsername(),
                    passwordEncoder.encode(registerRequest.getPassword()));

            // Получаем JWT
            JwtResponse token = authService.login(registerRequest.getUsername());
            return ResponseEntity.ok(token);

        } catch (Exception ex) {
            log.error("Registration error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed"));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        log.info("Login attempt: {}", authRequest.getUsername());
        try {
            // Аутентификация пользователя
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            // Получение JWT через сервис
            final JwtResponse token = authService.login(authRequest.getUsername());
            return ResponseEntity.ok(token);

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            log.warn("Authentication failed for user: {}", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        if (token.getAccessToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshJwtRequest request) {
        try {
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException ex) {
            log.warn("Failed to refresh token: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshJwtRequest request) {
        // Удаление refresh токена из хранилища
        authService.getAccessToken(request.getRefreshToken()); // просто проверка токена
        return ResponseEntity.ok("Logout successful");
    }
}
