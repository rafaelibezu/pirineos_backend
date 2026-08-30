package com.lospirineos.api.dashboard.dto.response;

import java.math.BigDecimal;

public record DashboardWaiterResponse(

        Long waiterId,
        String waiterName,

        Long totalClosings,

        BigDecimal totalSales,
        BigDecimal totalManual,

        BigDecimal cashDifference,
        BigDecimal posDifference,
        BigDecimal totalDifference
) {
}