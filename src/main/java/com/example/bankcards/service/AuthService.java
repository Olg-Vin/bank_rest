package com.example.bankcards.service;


import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.security.jwt.JwtResponse;
import com.example.bankcards.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserDetailsServiceImpl userDetailsServ;
    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();
    //    Для хранения рефреш токена используется HashMap лишь для упрощения примера.
    @Autowired
    public AuthService(UserDetailsServiceImpl userDetailsServ, JwtProvider jwtProvider) {
        this.userDetailsServ = userDetailsServ;
        this.jwtProvider = jwtProvider;
    }

    public JwtResponse login(@NonNull String username) {
        final UserDetails userDetails = userDetailsServ.loadUserByUsername(username);
        final String accessToken = jwtProvider.generateAccessToken(userDetails);
        final String refreshToken = jwtProvider.generateRefreshToken(userDetails);
        refreshStorage.put(userDetails.getUsername(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.isRefreshTokenValid(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserDetails userDetails = userDetailsServ.loadUserByUsername(login);
                final String accessToken = jwtProvider.generateAccessToken(userDetails);
                return new JwtResponse(accessToken, refreshToken);
            }
        }
        return new JwtResponse(null, null);
    }

    /**
     * Позволяет обновить refresh токен.
     * */
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.isRefreshTokenValid(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserDetails userDetails = userDetailsServ.loadUserByUsername(login);
                final String accessToken = jwtProvider.generateAccessToken(userDetails);
                final String newRefreshToken = jwtProvider.generateRefreshToken(userDetails);
                refreshStorage.put(userDetails.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }
}
