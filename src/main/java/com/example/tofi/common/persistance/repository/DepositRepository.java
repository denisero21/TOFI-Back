package com.example.tofi.common.persistance.repository;

import com.example.tofi.common.persistance.domain.depositservice.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit,Long> {
    List<Deposit> findAllByUserId(Long userId);
}
