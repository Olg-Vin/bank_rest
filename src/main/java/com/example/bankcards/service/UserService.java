package com.example.bankcards.service;

import com.example.bankcards.entity.UserRole;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.exceptions.RoleNotFoundException;
import com.example.bankcards.exception.exceptions.UserNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public User getUserWithRoles(Long userId) {
        return userRepository.findUserWithRoles(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void assignRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserRole userRole = roleRepository.findUserRoleByRoleName(Role.valueOf(roleName))
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        user.getRoles().add(userRole);
        userRepository.save(user);
    }
}