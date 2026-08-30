package com.lospirineos.api.dashboard.dto.response;

import java.time.LocalDate;
import java.util.List;

public record AdminDashboardResponse(

        LocalDate from,
        LocalDate to,

        DashboardSummaryResponse summary,

        List<DashboardShiftResponse> shifts,

        List<DashboardCashRegisterResponse> cashRegisters,

        List<DashboardWaiterResponse> waiters,

        List<DashboardDistributorResponse> distributors
) {
}