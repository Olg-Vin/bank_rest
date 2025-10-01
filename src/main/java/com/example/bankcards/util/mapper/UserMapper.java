package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword()); // Можно исключить для безопасности
        return dto;
    }

    public User toEntity(UserDto dto, UserRole role) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(role);
        return user;
    }
}

