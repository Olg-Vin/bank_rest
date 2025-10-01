package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.UserRole;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("pass");

        userDto = new UserDto();
        userDto.setUsername("john");
    }

    @Test
    void createUser_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("pass");

        UserDto userDto = new UserDto();
        userDto.setUsername("john");

        UserRole userRole = new UserRole();

        // Мокируем репозитории
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(roleRepository.findUserRoleByRoleName(Role.ROLE_USER)).thenReturn(Optional.of(userRole));

        UserDto result = userService.createUser("john", "pass");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepository, times(2)).save(any(User.class));
    }


    @Test
    void getUser_notFound() {
        when(userRepository.findUserWithRoles(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }
}

