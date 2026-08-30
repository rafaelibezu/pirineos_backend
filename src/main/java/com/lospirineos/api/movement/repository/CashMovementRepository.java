package com.lospirineos.api.movement.repository;

import com.lospirineos.api.movement.entity.CashMovement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface CashMovementRepository
        extends JpaRepository<CashMovement, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "shift",
            "user"
    })
    List<CashMovement> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "shift",
            "user"
    })
    Optional<CashMovement> findById(Long id);

    @EntityGraph(attributePaths = {
            "shift",
            "user"
    })
    List<CashMovement> findAllByUserId(
            Long userId
    );

    List<CashMovement> findAllByMovementAtBetween(
            OffsetDateTime start,
            OffsetDateTime end
    );

    List<CashMovement> findAllByUserIdAndMovementAtBetween(
            Long userId,
            OffsetDateTime start,
            OffsetDateTime end
    );

    // =========================================================
    // DASHBOARD CAMARERO - MOVIMIENTOS RECIENTES
    // =========================================================

    @EntityGraph(attributePaths = {
            "shift",
            "user"
    })
    List<CashMovement> findTop5ByUserIdOrderByMovementAtDesc(
            Long userId
    );
}