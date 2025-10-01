package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.UserRole;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.exceptions.RoleNotFoundException;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user = userRepository.save(user);

        assignRoleToUser(user.getId(), "ROLE_USER");

        return userMapper.toDto(userRepository.getReferenceById(user.getId()));
    }

    public UserDto getUser(Long userId) {
        User user = userRepository.findUserWithRoles(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    public boolean existUserByName(String userName) {
        return userRepository.findByUsername(userName).isPresent();
    }

    public void assignRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserRole userRole = roleRepository.findUserRoleByRoleName(Role.valueOf(roleName))
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        user.setRole(userRole);
        userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto updateUser(Long userId, String username, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(username);
        user.setPassword(password);
        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
