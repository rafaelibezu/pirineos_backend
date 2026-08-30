package com.lospirineos.api.dashboard.controller;

import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.dashboard.dto.response.WaiterDashboardResponse;
import com.lospirineos.api.dashboard.service.WaiterDashboardService;
import com.lospirineos.api.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/dashboard/waiter")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('WAITER', 'ADMIN')")
@Tag(
        name = "Dashboard Camarero",
        description = "Resumen personal del camarero autenticado"
)
public class WaiterDashboardController {

    private final WaiterDashboardService waiterDashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<WaiterDashboardResponse>> getDashboard(

            @AuthenticationPrincipal
            AuthenticatedUser authenticatedUser,

            @RequestParam(required = false)
            LocalDate date
    ) {

        WaiterDashboardResponse dashboard =
                waiterDashboardService.getDashboard(
                        authenticatedUser.getId(),
                        date
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Dashboard del camarero obtenido correctamente",
                        dashboard
                )
        );
    }
}