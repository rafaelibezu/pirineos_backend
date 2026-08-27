package com.lospirineos.api.cashregister.repository;

import com.lospirineos.api.cashregister.entity.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {

    Optional<CashRegister> findByCode(String code);

    List<CashRegister> findAllByActiveTrue();

    boolean existsByCode(String code);
}