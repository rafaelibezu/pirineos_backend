package com.lospirineos.api.dashboard.dto.response;

import java.math.BigDecimal;

public record DashboardShiftResponse(

        Long shiftId,
        String shiftCode,
        String shiftName,

        Long totalClosings,

        BigDecimal totalSales,
        BigDecimal totalManual,
        BigDecimal totalCash,
        BigDecimal totalPayments
) {
}