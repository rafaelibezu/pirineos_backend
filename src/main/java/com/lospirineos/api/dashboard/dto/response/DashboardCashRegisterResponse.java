package com.lospirineos.api.dashboard.dto.response;

import java.math.BigDecimal;

public record DashboardCashRegisterResponse(

        Long cashRegisterId,
        String cashRegisterCode,
        String cashRegisterName,

        Long totalClosings,

        BigDecimal totalSales,
        BigDecimal totalCash,
        BigDecimal totalDifference
) {
}