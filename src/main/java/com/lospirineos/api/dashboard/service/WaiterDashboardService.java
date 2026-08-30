package com.lospirineos.api.dashboard.service;

import com.lospirineos.api.dashboard.dto.response.WaiterDashboardResponse;

import java.time.LocalDate;

public interface WaiterDashboardService {

    WaiterDashboardResponse getDashboard(
            Long authenticatedUserId,
            LocalDate date
    );
}