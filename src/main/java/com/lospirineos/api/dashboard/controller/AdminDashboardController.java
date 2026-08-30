package com.lospirineos.api.dashboard.controller;

import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.dashboard.dto.response.AdminDashboardResponse;
import com.lospirineos.api.dashboard.service.AdminDashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/dashboard/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(
        name = "Dashboard Admin",
        description = "Indicadores y reportes generales del negocio"
)
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard(

            @RequestParam LocalDate from,

            @RequestParam LocalDate to,

            @RequestParam(required = false)
            Long waiterId,

            @RequestParam(required = false)
            Long distributorId,

            @RequestParam(required = false)
            Long shiftId,

            @RequestParam(required = false)
            Long cashRegisterId
    ) {

        AdminDashboardResponse dashboard =
                adminDashboardService.getDashboard(
                        from,
                        to,
                        waiterId,
                        distributorId,
                        shiftId,
                        cashRegisterId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Dashboard obtenido correctamente",
                        dashboard
                )
        );
    }
}