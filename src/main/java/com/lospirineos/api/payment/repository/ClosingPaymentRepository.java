package com.lospirineos.api.payment.repository;

import com.lospirineos.api.common.enums.ClosingStatus;
import com.lospirineos.api.dashboard.projection.DashboardDistributorProjection;
import com.lospirineos.api.payment.entity.ClosingPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ClosingPaymentRepository
        extends JpaRepository<ClosingPayment, Long> {

    List<ClosingPayment> findAllByDailyClosingId(Long dailyClosingId);

    List<ClosingPayment> findAllByDistributorId(Long distributorId);

    @Query("""
            SELECT
                cp.distributor.id AS distributorId,
                cp.distributor.name AS distributorName,
                COUNT(cp.id) AS totalInvoices,
                SUM(cp.amount) AS totalPaid
            FROM ClosingPayment cp
            WHERE cp.invoiceDate >= :from
              AND cp.invoiceDate <= :to
              AND cp.dailyClosing.status = :status
              AND (
                    :distributorId IS NULL
                    OR cp.distributor.id = :distributorId
                  )
            GROUP BY
                cp.distributor.id,
                cp.distributor.name
            ORDER BY SUM(cp.amount) DESC
            """)
    List<DashboardDistributorProjection> getDashboardByDistributor(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("status") ClosingStatus status,
            @Param("distributorId") Long distributorId
    );
}