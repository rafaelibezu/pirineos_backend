package com.lospirineos.api.dashboard.service.impl;

import com.lospirineos.api.closing.entity.DailyClosing;
import com.lospirineos.api.closing.repository.DailyClosingRepository;
import com.lospirineos.api.common.enums.ClosingStatus;
import com.lospirineos.api.common.exception.ResourceNotFoundException;
import com.lospirineos.api.dashboard.dto.response.WaiterClosingResponse;
import com.lospirineos.api.dashboard.dto.response.WaiterDashboardResponse;
import com.lospirineos.api.dashboard.dto.response.WaiterDashboardSummaryResponse;
import com.lospirineos.api.dashboard.dto.response.WaiterMovementResponse;
import com.lospirineos.api.dashboard.dto.response.WaiterTransferResponse;
import com.lospirineos.api.dashboard.service.WaiterDashboardService;
import com.lospirineos.api.movement.entity.CashMovement;
import com.lospirineos.api.movement.repository.CashMovementRepository;
import com.lospirineos.api.transfer.entity.CashTransfer;
import com.lospirineos.api.transfer.repository.CashTransferRepository;
import com.lospirineos.api.user.entity.User;
import com.lospirineos.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaiterDashboardServiceImpl
        implements WaiterDashboardService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private static final ZoneId BUSINESS_ZONE =
            ZoneId.of("Europe/Madrid");

    private final UserRepository userRepository;

    private final DailyClosingRepository dailyClosingRepository;

    private final CashMovementRepository cashMovementRepository;

    private final CashTransferRepository cashTransferRepository;

    @Override
    public WaiterDashboardResponse getDashboard(
            Long authenticatedUserId,
            LocalDate date
    ) {

        User waiter = userRepository.findById(authenticatedUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El usuario autenticado no existe"
                        )
                );

        LocalDate dashboardDate =
                date != null
                        ? date
                        : LocalDate.now(BUSINESS_ZONE);

        OffsetDateTime start =
                dashboardDate
                        .atStartOfDay(BUSINESS_ZONE)
                        .toOffsetDateTime();

        OffsetDateTime end =
                dashboardDate
                        .plusDays(1)
                        .atStartOfDay(BUSINESS_ZONE)
                        .toOffsetDateTime();

        // =====================================================
        // CIERRES DEL DÍA
        // =====================================================

        List<DailyClosing> todayClosingEntities =
                dailyClosingRepository
                        .findAllByWaiterIdAndClosingAtBetweenOrderByClosingAtDesc(
                                authenticatedUserId,
                                start,
                                end
                        );

        List<WaiterClosingResponse> todayClosings =
                todayClosingEntities
                        .stream()
                        .map(this::toClosingResponse)
                        .toList();

        // =====================================================
        // RESUMEN DEL DÍA
        // =====================================================

        WaiterDashboardSummaryResponse summary =
                buildSummary(todayClosingEntities);

        // =====================================================
        // ÚLTIMOS CIERRES
        // =====================================================

        List<WaiterClosingResponse> recentClosings =
                dailyClosingRepository
                        .findTop5ByWaiterIdOrderByClosingAtDesc(
                                authenticatedUserId
                        )
                        .stream()
                        .map(this::toClosingResponse)
                        .toList();

        // =====================================================
        // MOVIMIENTOS RECIENTES
        // =====================================================

        List<WaiterMovementResponse> recentMovements =
                cashMovementRepository
                        .findTop5ByUserIdOrderByMovementAtDesc(
                                authenticatedUserId
                        )
                        .stream()
                        .map(this::toMovementResponse)
                        .toList();

        // =====================================================
        // TRASPASOS RECIENTES
        // =====================================================

        List<WaiterTransferResponse> recentTransfers =
                cashTransferRepository
                        .findTop5ByResponsibleUserIdOrderByTransferAtDesc(
                                authenticatedUserId
                        )
                        .stream()
                        .map(this::toTransferResponse)
                        .toList();

        String waiterName =
                buildUserName(waiter);

        return new WaiterDashboardResponse(
                dashboardDate,
                waiter.getId(),
                waiterName,
                summary,
                todayClosings,
                recentClosings,
                recentMovements,
                recentTransfers
        );
    }

    // =========================================================
    // RESUMEN
    // =========================================================

    private WaiterDashboardSummaryResponse buildSummary(
            List<DailyClosing> closings
    ) {

        BigDecimal totalSales = ZERO;
        BigDecimal totalManual = ZERO;
        BigDecimal totalPayments = ZERO;

        BigDecimal cashDifference = ZERO;
        BigDecimal posDifference = ZERO;

        long finalizedClosings = 0L;

        boolean hasPendingClosing = false;

        for (DailyClosing closing : closings) {

            if (closing.getStatus() == ClosingStatus.DRAFT) {
                hasPendingClosing = true;
            }

            /*
             * Para los importes oficiales del resumen del día
             * contamos solamente los cierres FINALIZED.
             */
            if (closing.getStatus() != ClosingStatus.FINALIZED) {
                continue;
            }

            finalizedClosings++;

            totalSales = totalSales.add(
                    valueOrZero(
                            closing.getZClosing()
                    )
            );

            totalManual = totalManual.add(
                    valueOrZero(
                            closing.getManualAmount()
                    )
            );

            totalPayments = totalPayments.add(
                    valueOrZero(
                            closing.getTotalPayments()
                    )
            );

            cashDifference = cashDifference.add(
                    valueOrZero(
                            closing.getCashDifference()
                    )
            );

            posDifference = posDifference.add(
                    valueOrZero(
                            closing.getPosDifference()
                    )
            );
        }

        BigDecimal totalDifference =
                cashDifference.add(posDifference);

        return new WaiterDashboardSummaryResponse(
                finalizedClosings,
                totalSales,
                totalManual,
                totalPayments,
                cashDifference,
                posDifference,
                totalDifference,
                hasPendingClosing
        );
    }

    // =========================================================
    // CLOSING
    // =========================================================

    private WaiterClosingResponse toClosingResponse(
            DailyClosing closing
    ) {

        BigDecimal cashDifference =
                valueOrZero(
                        closing.getCashDifference()
                );

        BigDecimal posDifference =
                valueOrZero(
                        closing.getPosDifference()
                );

        return new WaiterClosingResponse(
                closing.getId(),

                closing.getClosingAt(),

                closing.getShift().getId(),
                closing.getShift().getName(),

                closing.getCashRegister().getId(),
                closing.getCashRegister().getName(),

                valueOrZero(
                        closing.getZClosing()
                ),

                valueOrZero(
                        closing.getManualAmount()
                ),

                valueOrZero(
                        closing.getDeclaredVisa()
                ),

                valueOrZero(
                        closing.getTotalPayments()
                ),

                valueOrZero(
                        closing.getCashTotal()
                ),

                valueOrZero(
                        closing.getRealPos()
                ),

                cashDifference,

                posDifference,

                cashDifference.add(posDifference),

                closing.getStatus()
        );
    }

    // =========================================================
    // MOVEMENT
    // =========================================================

    private WaiterMovementResponse toMovementResponse(
            CashMovement movement
    ) {

        return new WaiterMovementResponse(
                movement.getId(),

                movement.getMovementAt(),

                movement.getMovementType(),

                movement.getConcept(),

                movement.getAccount(),

                movement.getAccount().getDisplayName(),

                movement.getBeneficiary(),

                valueOrZero(
                        movement.getAmount()
                ),

                movement.getShift().getName()
        );
    }

    // =========================================================
    // TRANSFER
    // =========================================================

    private WaiterTransferResponse toTransferResponse(
            CashTransfer transfer
    ) {

        return new WaiterTransferResponse(
                transfer.getId(),

                transfer.getTransferAt(),

                transfer.getShift().getName(),

                transfer.getSourceCashRegister().getId(),
                transfer.getSourceCashRegister().getName(),

                transfer.getDestinationCashRegister().getId(),
                transfer.getDestinationCashRegister().getName(),

                valueOrZero(
                        transfer.getAmount()
                ),

                transfer.getReason()
        );
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private String buildUserName(
            User user
    ) {

        String firstName =
                user.getFirstName() != null
                        ? user.getFirstName()
                        : "";

        String lastName =
                user.getLastName() != null
                        ? user.getLastName()
                        : "";

        return (firstName + " " + lastName).trim();
    }

    private BigDecimal valueOrZero(
            BigDecimal value
    ) {

        return value != null
                ? value
                : ZERO;
    }
}