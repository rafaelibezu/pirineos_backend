package com.lospirineos.api.dashboard.service.impl;

import com.lospirineos.api.closing.repository.DailyClosingRepository;
import com.lospirineos.api.common.enums.ClosingStatus;
import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.dashboard.dto.response.AdminDashboardResponse;
import com.lospirineos.api.dashboard.dto.response.DashboardCashRegisterResponse;
import com.lospirineos.api.dashboard.dto.response.DashboardDistributorResponse;
import com.lospirineos.api.dashboard.dto.response.DashboardShiftResponse;
import com.lospirineos.api.dashboard.dto.response.DashboardSummaryResponse;
import com.lospirineos.api.dashboard.dto.response.DashboardWaiterResponse;
import com.lospirineos.api.dashboard.projection.DashboardCashRegisterProjection;
import com.lospirineos.api.dashboard.projection.DashboardDistributorProjection;
import com.lospirineos.api.dashboard.projection.DashboardShiftProjection;
import com.lospirineos.api.dashboard.projection.DashboardSummaryProjection;
import com.lospirineos.api.dashboard.projection.DashboardWaiterProjection;
import com.lospirineos.api.dashboard.service.AdminDashboardService;
import com.lospirineos.api.payment.repository.ClosingPaymentRepository;
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
public class AdminDashboardServiceImpl
        implements AdminDashboardService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    /*
     * El negocio está en España.
     *
     * De esta manera cuando el administrador consulta, por ejemplo,
     * 29/08/2026, tomamos el día completo según hora española.
     */
    private static final ZoneId BUSINESS_ZONE =
            ZoneId.of("Europe/Madrid");

    private final DailyClosingRepository dailyClosingRepository;
    private final ClosingPaymentRepository closingPaymentRepository;

    @Override
    public AdminDashboardResponse getDashboard(
            LocalDate from,
            LocalDate to,
            Long waiterId,
            Long distributorId,
            Long shiftId,
            Long cashRegisterId
    ) {

        validateDates(from, to);

        /*
         * INICIO:
         * 29/08/2026 00:00 Madrid
         */
        OffsetDateTime start =
                from
                        .atStartOfDay(BUSINESS_ZONE)
                        .toOffsetDateTime();

        /*
         * FIN EXCLUSIVO:
         *
         * Si to = 29/08/2026
         * entonces end = 30/08/2026 00:00.
         *
         * Consultamos:
         *
         * closingAt >= start
         * closingAt < end
         */
        OffsetDateTime end =
                to
                        .plusDays(1)
                        .atStartOfDay(BUSINESS_ZONE)
                        .toOffsetDateTime();

        /*
         * Para cifras oficiales del dashboard utilizamos
         * únicamente cierres FINALIZED.
         *
         * Un DRAFT todavía podría cambiar.
         */
        ClosingStatus status = ClosingStatus.FINALIZED;

        // =====================================================
        // RESUMEN GENERAL
        // =====================================================

        DashboardSummaryProjection summaryProjection =
                dailyClosingRepository.getDashboardSummary(
                        start,
                        end,
                        status,
                        waiterId,
                        shiftId,
                        cashRegisterId
                );

        DashboardSummaryResponse summary =
                buildSummary(summaryProjection);

        // =====================================================
        // TURNOS
        // =====================================================

        List<DashboardShiftResponse> shifts =
                dailyClosingRepository
                        .getDashboardByShift(
                                start,
                                end,
                                status,
                                waiterId,
                                cashRegisterId
                        )
                        .stream()
                        .map(this::toShiftResponse)
                        .toList();

        // =====================================================
        // CAJAS
        // =====================================================

        List<DashboardCashRegisterResponse> cashRegisters =
                dailyClosingRepository
                        .getDashboardByCashRegister(
                                start,
                                end,
                                status,
                                waiterId,
                                shiftId
                        )
                        .stream()
                        .map(this::toCashRegisterResponse)
                        .toList();

        // =====================================================
        // CAMAREROS
        // =====================================================

        List<DashboardWaiterResponse> waiters =
                dailyClosingRepository
                        .getDashboardByWaiter(
                                start,
                                end,
                                status,
                                shiftId,
                                cashRegisterId
                        )
                        .stream()
                        .map(this::toWaiterResponse)
                        .toList();

        // =====================================================
        // DISTRIBUIDORES / FACTURAS
        // =====================================================

        List<DashboardDistributorResponse> distributors =
                closingPaymentRepository
                        .getDashboardByDistributor(
                                from,
                                to,
                                status,
                                distributorId
                        )
                        .stream()
                        .map(this::toDistributorResponse)
                        .toList();

        return new AdminDashboardResponse(
                from,
                to,
                summary,
                shifts,
                cashRegisters,
                waiters,
                distributors
        );
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    private DashboardSummaryResponse buildSummary(
            DashboardSummaryProjection projection
    ) {

        BigDecimal cashDifference =
                valueOrZero(
                        projection.getTotalCashDifference()
                );

        BigDecimal posDifference =
                valueOrZero(
                        projection.getTotalPosDifference()
                );

        BigDecimal totalDifference =
                cashDifference.add(posDifference);

        return new DashboardSummaryResponse(
                projection.getTotalClosings() != null
                        ? projection.getTotalClosings()
                        : 0L,

                valueOrZero(
                        projection.getTotalSales()
                ),

                valueOrZero(
                        projection.getTotalManual()
                ),

                valueOrZero(
                        projection.getTotalCash()
                ),

                valueOrZero(
                        projection.getTotalDeclaredVisa()
                ),

                valueOrZero(
                        projection.getTotalRealPos()
                ),

                valueOrZero(
                        projection.getTotalDistributorPayments()
                ),

                cashDifference,
                posDifference,
                totalDifference
        );
    }

    // =========================================================
    // SHIFT
    // =========================================================

    private DashboardShiftResponse toShiftResponse(
            DashboardShiftProjection projection
    ) {

        return new DashboardShiftResponse(
                projection.getShiftId(),
                projection.getShiftCode(),
                projection.getShiftName(),

                projection.getTotalClosings() != null
                        ? projection.getTotalClosings()
                        : 0L,

                valueOrZero(
                        projection.getTotalSales()
                ),

                valueOrZero(
                        projection.getTotalManual()
                ),

                valueOrZero(
                        projection.getTotalCash()
                ),

                valueOrZero(
                        projection.getTotalPayments()
                )
        );
    }

    // =========================================================
    // CASH REGISTER
    // =========================================================

    private DashboardCashRegisterResponse toCashRegisterResponse(
            DashboardCashRegisterProjection projection
    ) {

        BigDecimal cashDifference =
                valueOrZero(
                        projection.getCashDifference()
                );

        BigDecimal posDifference =
                valueOrZero(
                        projection.getPosDifference()
                );

        return new DashboardCashRegisterResponse(
                projection.getCashRegisterId(),
                projection.getCashRegisterCode(),
                projection.getCashRegisterName(),

                projection.getTotalClosings() != null
                        ? projection.getTotalClosings()
                        : 0L,

                valueOrZero(
                        projection.getTotalSales()
                ),

                valueOrZero(
                        projection.getTotalCash()
                ),

                cashDifference.add(posDifference)
        );
    }

    // =========================================================
    // WAITER
    // =========================================================

    private DashboardWaiterResponse toWaiterResponse(
            DashboardWaiterProjection projection
    ) {

        String firstName =
                projection.getFirstName() != null
                        ? projection.getFirstName()
                        : "";

        String lastName =
                projection.getLastName() != null
                        ? projection.getLastName()
                        : "";

        String fullName =
                (firstName + " " + lastName).trim();

        BigDecimal cashDifference =
                valueOrZero(
                        projection.getCashDifference()
                );

        BigDecimal posDifference =
                valueOrZero(
                        projection.getPosDifference()
                );

        return new DashboardWaiterResponse(
                projection.getWaiterId(),
                fullName,

                projection.getTotalClosings() != null
                        ? projection.getTotalClosings()
                        : 0L,

                valueOrZero(
                        projection.getTotalSales()
                ),

                valueOrZero(
                        projection.getTotalManual()
                ),

                cashDifference,
                posDifference,
                cashDifference.add(posDifference)
        );
    }

    // =========================================================
    // DISTRIBUTOR
    // =========================================================

    private DashboardDistributorResponse toDistributorResponse(
            DashboardDistributorProjection projection
    ) {

        return new DashboardDistributorResponse(
                projection.getDistributorId(),
                projection.getDistributorName(),

                projection.getTotalInvoices() != null
                        ? projection.getTotalInvoices()
                        : 0L,

                valueOrZero(
                        projection.getTotalPaid()
                )
        );
    }

    // =========================================================
    // UTILS
    // =========================================================

    private BigDecimal valueOrZero(
            BigDecimal value
    ) {

        return value != null
                ? value
                : ZERO;
    }

    private void validateDates(
            LocalDate from,
            LocalDate to
    ) {

        if (from == null || to == null) {
            throw new BusinessException(
                    "Las fechas de inicio y fin son obligatorias"
            );
        }

        if (from.isAfter(to)) {
            throw new BusinessException(
                    "La fecha de inicio no puede ser posterior a la fecha final"
            );
        }
    }
}