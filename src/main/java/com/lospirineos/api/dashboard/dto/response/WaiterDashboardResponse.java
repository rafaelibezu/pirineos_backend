package com.lospirineos.api.dashboard.dto.response;

import java.time.LocalDate;
import java.util.List;

public record WaiterDashboardResponse(

        LocalDate date,

        Long waiterId,

        String waiterName,

        WaiterDashboardSummaryResponse summary,

        List<WaiterClosingResponse> todayClosings,

        List<WaiterClosingResponse> recentClosings,

        List<WaiterMovementResponse> recentMovements,

        List<WaiterTransferResponse> recentTransfers
) {
}