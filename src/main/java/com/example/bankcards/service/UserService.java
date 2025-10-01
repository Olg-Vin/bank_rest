package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.UserRole;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.exceptions.RoleNotFoundException;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user = userRepository.save(user);
        assignRoleToUser(user.getId(), "ROLE_USER");
        return modelMapper.map(userRepository.getReferenceById(user.getId()), UserDto.class);
    }

    public UserDto getUser(Long userId) {
        return modelMapper.map(
                userRepository.findUserWithRoles(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found")),
                UserDto.class);
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
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    public UserDto updateUser(Long userId, String username, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(username);
        user.setPassword(password);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
}