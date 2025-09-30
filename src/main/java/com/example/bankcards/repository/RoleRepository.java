package com.example.bankcards.repository;

import com.example.bankcards.entity.UserRole;
import com.example.bankcards.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, String> {
    Optional<UserRole> findUserRoleByRoleName(Role roleName);
}