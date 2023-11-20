package com.example.tofi.common.persistance.repository;

import com.example.tofi.common.persistance.domain.userservice.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByIdIsIn(Set<Long> ids);

    boolean existsByNameIgnoreCase(String name);
}