package com.enigma.ClassNexa.repository;

import com.enigma.ClassNexa.constan.ERole;
import com.enigma.ClassNexa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(ERole role);
}
