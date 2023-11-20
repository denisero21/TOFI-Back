package com.example.tofi.common.persistance.repository;

import com.example.tofi.common.persistance.domain.userservice.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional
public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    Optional<SmsCode> findSmsCodesByUserId(Long userId);

    void deleteByUserId(Long userId);





}