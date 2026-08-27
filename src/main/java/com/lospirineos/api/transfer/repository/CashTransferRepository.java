package com.lospirineos.api.transfer.repository;

import com.lospirineos.api.transfer.entity.CashTransfer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashTransferRepository
        extends JpaRepository<CashTransfer, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "shift",
            "responsibleUser",
            "sourceCashRegister",
            "destinationCashRegister"
    })
    List<CashTransfer> findAll();

    @EntityGraph(attributePaths = {
            "shift",
            "responsibleUser",
            "sourceCashRegister",
            "destinationCashRegister"
    })
    List<CashTransfer> findAllByResponsibleUserId(
            Long responsibleUserId
    );
}