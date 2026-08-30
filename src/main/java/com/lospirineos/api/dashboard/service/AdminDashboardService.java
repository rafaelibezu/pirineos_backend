package com.lospirineos.api.dashboard.service;

import com.lospirineos.api.dashboard.dto.response.AdminDashboardResponse;

import java.time.LocalDate;

public interface AdminDashboardService {

    AdminDashboardResponse getDashboard(
            LocalDate from,
            LocalDate to,
            Long waiterId,
            Long distributorId,
            Long shiftId,
            Long cashRegisterId
    );
}