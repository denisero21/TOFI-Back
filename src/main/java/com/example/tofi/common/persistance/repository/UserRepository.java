package com.example.tofi.common.persistance.repository;

import com.example.tofi.common.persistance.domain.user.Role;
import com.example.tofi.common.persistance.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User u set u.isEnabled = ?1 where u.id = ?2")
    void setStatusForUser(boolean enabled, Long id);

    @Modifying
    @Query("update User u set u.isEnabled = ?1 where u.id IN ?2")
    void setStatusesForUsers(boolean enabled, Set<Long> ids);

    boolean existsByEmailAndRolesContaining(String email, Role role);

    boolean existsByRolesIdIn(Set<Long> roleIds);

    List<User> findAllByRolesContaining(Role role);

    boolean existsByEmail(String email);
}
