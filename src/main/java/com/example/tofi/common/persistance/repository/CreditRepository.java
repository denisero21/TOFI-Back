package com.example.tofi.common.persistance.repository;

import com.example.tofi.common.persistance.domain.creditservice.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit,Long> {
}
