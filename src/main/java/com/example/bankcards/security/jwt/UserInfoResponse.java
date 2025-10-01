package com.example.bankcards.security.jwt;

import com.example.bankcards.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    JwtResponse jwtResponse;
    UserDto userDTO;
}
