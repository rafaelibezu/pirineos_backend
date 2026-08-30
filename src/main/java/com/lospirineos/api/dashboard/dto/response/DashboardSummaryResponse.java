package com.lospirineos.api.dashboard.dto.response;

import java.math.BigDecimal;

public record DashboardSummaryResponse(

        Long totalClosings,

        BigDecimal totalSales,
        BigDecimal totalManual,

        BigDecimal totalCash,
        BigDecimal totalDeclaredVisa,
        BigDecimal totalRealPos,

        BigDecimal totalDistributorPayments,

        BigDecimal totalCashDifference,
        BigDecimal totalPosDifference,
        BigDecimal totalDifference
) {
}