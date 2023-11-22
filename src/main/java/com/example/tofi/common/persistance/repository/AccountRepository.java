package com.example.tofi.common.persistance.repository;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findAllByUserId(Long userId);
}
