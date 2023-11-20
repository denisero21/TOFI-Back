package com.example.tofi.common.persistance.repository;

import com.example.tofi.common.persistance.domain.authservice.LockedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LockedUserRepository extends JpaRepository<LockedUser,Long> {
    Optional<LockedUser> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    @Transactional
    @Modifying
    @Query("update LockedUser set userLocked = false , attempt = 1 ,lockTime = null where id = :id")
    void unLockUserById(@Param("id") Long id);


}
