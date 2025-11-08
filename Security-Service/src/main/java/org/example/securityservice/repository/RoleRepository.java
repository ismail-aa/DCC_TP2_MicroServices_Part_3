package org.example.securityservice.repository;

import org.example.securityservice.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<AppRole, Long> {
    Optional<AppRole> findByRoleName(String roleName);
}