package com.lospirineos.api.dashboard.dto.response;

import java.math.BigDecimal;

public record DashboardDistributorResponse(

        Long distributorId,
        String distributorName,

        Long totalInvoices,
        BigDecimal totalPaid
) {
}