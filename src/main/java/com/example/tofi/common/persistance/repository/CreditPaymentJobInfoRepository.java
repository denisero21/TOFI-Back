package com.example.tofi.common.persistance.repository;

import com.example.tofi.common.persistance.domain.creditservice.CreditPaymentJobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditPaymentJobInfoRepository extends JpaRepository<CreditPaymentJobInfo,Long> {
    Optional<CreditPaymentJobInfo> findByCreditId(Long creditId);
}
