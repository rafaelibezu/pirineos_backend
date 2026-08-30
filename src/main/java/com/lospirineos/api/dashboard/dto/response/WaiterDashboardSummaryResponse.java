package com.lospirineos.api.dashboard.dto.response;

import java.math.BigDecimal;

public record WaiterDashboardSummaryResponse(

        Long totalClosingsToday,

        BigDecimal totalSales,
        BigDecimal totalManual,
        BigDecimal totalPayments,

        BigDecimal cashDifference,
        BigDecimal posDifference,
        BigDecimal totalDifference,

        boolean hasPendingClosing
) {
}