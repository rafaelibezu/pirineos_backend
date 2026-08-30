package com.lospirineos.api.closing.repository;

import com.lospirineos.api.closing.entity.DailyClosing;
import com.lospirineos.api.common.enums.ClosingStatus;
import com.lospirineos.api.dashboard.projection.DashboardCashRegisterProjection;
import com.lospirineos.api.dashboard.projection.DashboardShiftProjection;
import com.lospirineos.api.dashboard.projection.DashboardSummaryProjection;
import com.lospirineos.api.dashboard.projection.DashboardWaiterProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface DailyClosingRepository
        extends JpaRepository<DailyClosing, Long> {


    List<DailyClosing> findAllByStatus(ClosingStatus status);

    List<DailyClosing> findAllByWaiterId(Long waiterId);

    List<DailyClosing> findAllByCashRegisterId(Long cashRegisterId);

    List<DailyClosing> findAllByShiftId(Long shiftId);

    List<DailyClosing> findAllByClosingAtBetween(
            OffsetDateTime start,
            OffsetDateTime end
    );


    @Query("""
            SELECT
                COUNT(dc.id) AS totalClosings,
                SUM(dc.zClosing) AS totalSales,
                SUM(dc.manualAmount) AS totalManual,
                SUM(dc.cashTotal) AS totalCash,
                SUM(dc.declaredVisa) AS totalDeclaredVisa,
                SUM(dc.realPos) AS totalRealPos,
                SUM(dc.totalPayments) AS totalDistributorPayments,
                SUM(dc.cashDifference) AS totalCashDifference,
                SUM(dc.posDifference) AS totalPosDifference
            FROM DailyClosing dc
            WHERE dc.closingAt >= :start
              AND dc.closingAt < :end
              AND dc.status = :status
              AND (
                    :waiterId IS NULL
                    OR dc.waiter.id = :waiterId
                  )
              AND (
                    :shiftId IS NULL
                    OR dc.shift.id = :shiftId
                  )
              AND (
                    :cashRegisterId IS NULL
                    OR dc.cashRegister.id = :cashRegisterId
                  )
            """)
    DashboardSummaryProjection getDashboardSummary(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("status") ClosingStatus status,
            @Param("waiterId") Long waiterId,
            @Param("shiftId") Long shiftId,
            @Param("cashRegisterId") Long cashRegisterId
    );


    // =========================================================
    // DASHBOARD ADMIN - RESUMEN POR TURNO
    // =========================================================

    @Query("""
            SELECT
                dc.shift.id AS shiftId,
                dc.shift.code AS shiftCode,
                dc.shift.name AS shiftName,
                COUNT(dc.id) AS totalClosings,
                SUM(dc.zClosing) AS totalSales,
                SUM(dc.manualAmount) AS totalManual,
                SUM(dc.cashTotal) AS totalCash,
                SUM(dc.totalPayments) AS totalPayments
            FROM DailyClosing dc
            WHERE dc.closingAt >= :start
              AND dc.closingAt < :end
              AND dc.status = :status
              AND (
                    :waiterId IS NULL
                    OR dc.waiter.id = :waiterId
                  )
              AND (
                    :cashRegisterId IS NULL
                    OR dc.cashRegister.id = :cashRegisterId
                  )
            GROUP BY
                dc.shift.id,
                dc.shift.code,
                dc.shift.name
            ORDER BY dc.shift.id
            """)
    List<DashboardShiftProjection> getDashboardByShift(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("status") ClosingStatus status,
            @Param("waiterId") Long waiterId,
            @Param("cashRegisterId") Long cashRegisterId
    );


    // =========================================================
    // DASHBOARD ADMIN - RESUMEN POR CAJA
    // =========================================================

    @Query("""
            SELECT
                dc.cashRegister.id AS cashRegisterId,
                dc.cashRegister.code AS cashRegisterCode,
                dc.cashRegister.name AS cashRegisterName,
                COUNT(dc.id) AS totalClosings,
                SUM(dc.zClosing) AS totalSales,
                SUM(dc.cashTotal) AS totalCash,
                SUM(dc.cashDifference) AS cashDifference,
                SUM(dc.posDifference) AS posDifference
            FROM DailyClosing dc
            WHERE dc.closingAt >= :start
              AND dc.closingAt < :end
              AND dc.status = :status
              AND (
                    :waiterId IS NULL
                    OR dc.waiter.id = :waiterId
                  )
              AND (
                    :shiftId IS NULL
                    OR dc.shift.id = :shiftId
                  )
            GROUP BY
                dc.cashRegister.id,
                dc.cashRegister.code,
                dc.cashRegister.name
            ORDER BY dc.cashRegister.id
            """)
    List<DashboardCashRegisterProjection> getDashboardByCashRegister(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("status") ClosingStatus status,
            @Param("waiterId") Long waiterId,
            @Param("shiftId") Long shiftId
    );


    // =========================================================
    // DASHBOARD ADMIN - RESUMEN POR CAMARERO
    // =========================================================

    @Query("""
            SELECT
                dc.waiter.id AS waiterId,
                dc.waiter.firstName AS firstName,
                dc.waiter.lastName AS lastName,
                COUNT(dc.id) AS totalClosings,
                SUM(dc.zClosing) AS totalSales,
                SUM(dc.manualAmount) AS totalManual,
                SUM(dc.cashDifference) AS cashDifference,
                SUM(dc.posDifference) AS posDifference
            FROM DailyClosing dc
            WHERE dc.closingAt >= :start
              AND dc.closingAt < :end
              AND dc.status = :status
              AND (
                    :shiftId IS NULL
                    OR dc.shift.id = :shiftId
                  )
              AND (
                    :cashRegisterId IS NULL
                    OR dc.cashRegister.id = :cashRegisterId
                  )
            GROUP BY
                dc.waiter.id,
                dc.waiter.firstName,
                dc.waiter.lastName
            ORDER BY SUM(dc.zClosing) DESC
            """)

    List<DashboardWaiterProjection> getDashboardByWaiter(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("status") ClosingStatus status,
            @Param("shiftId") Long shiftId,
            @Param("cashRegisterId") Long cashRegisterId
    );

    List<DailyClosing> findAllByWaiterIdAndClosingAtBetweenOrderByClosingAtDesc(
            Long waiterId,
            OffsetDateTime start,
            OffsetDateTime end
    );

    List<DailyClosing> findTop5ByWaiterIdOrderByClosingAtDesc(
            Long waiterId
    );

}